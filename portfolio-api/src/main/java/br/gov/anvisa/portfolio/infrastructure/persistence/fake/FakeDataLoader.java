package br.gov.anvisa.portfolio.infrastructure.persistence.fake;

import br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
@Profile("dev")
public class FakeDataLoader {
    private final FakeDataStore store;
    private final ObjectMapper mapper;

    public FakeDataLoader(FakeDataStore store) {
        this.store = store;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void load() {
        try {
            loadList("dev/fake/catalogos.json", new TypeReference<List<FakeCatalogoDto>>() {}, store.catalogos::addAll);
            loadMap("dev/fake/sistemas.json", new TypeReference<List<FakeSistemaDto>>() {}, store.sistemas::put, store.sistemaSeq, FakeSistemaDto::id);
            loadMap("dev/fake/servidores.json", new TypeReference<List<FakeServidorDto>>() {}, store.servidores::put, store.servidorSeq, FakeServidorDto::id);
            loadMap("dev/fake/urls.json", new TypeReference<List<FakeUrlDto>>() {}, store.urls::put, store.urlSeq, FakeUrlDto::id);
            loadMap("dev/fake/componentes.json", new TypeReference<List<FakeComponenteDto>>() {}, store.componentes::put, store.componenteSeq, FakeComponenteDto::id);
            loadMap("dev/fake/documentacoes.json", new TypeReference<List<FakeDocumentacaoDto>>() {}, store.documentacoes::put, store.documentacaoSeq, FakeDocumentacaoDto::id);
            loadMap("dev/fake/rotinas.json", new TypeReference<List<FakeRotinaDto>>() {}, store.rotinas::put, store.rotinaSeq, FakeRotinaDto::id);
            loadMap("dev/fake/backups.json", new TypeReference<List<FakeBackupDto>>() {}, store.backups::put, store.backupSeq, FakeBackupDto::id);
            loadMap("dev/fake/usuarios.json", new TypeReference<List<FakeUsuarioDto>>() {}, store.usuarios::put, store.usuarioSeq, FakeUsuarioDto::id);
            loadMap("dev/fake/vinculos.json", new TypeReference<List<FakeVinculoDto>>() {}, store.vinculos::put, store.vinculoSeq, FakeVinculoDto::id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar dados fake", e);
        }
    }

    private <T> void loadList(String path, TypeReference<List<T>> ref, java.util.function.Consumer<List<T>> consumer) throws Exception {
        ClassPathResource res = new ClassPathResource(path);
        if (!res.exists()) return;
        try (InputStream in = res.getInputStream()) {
            List<T> list = mapper.readValue(in, ref);
            consumer.accept(list);
        }
    }

    private <T> void loadMap(String path, TypeReference<List<T>> ref, BiConsumer<Long, T> put, AtomicLong seq, Function<T, Long> idGetter) throws Exception {
        ClassPathResource res = new ClassPathResource(path);
        if (!res.exists()) return;
        try (InputStream in = res.getInputStream()) {
            List<T> list = mapper.readValue(in, ref);
            long max = 0;
            for (T dto : list) {
                Long id = idGetter.apply(dto);
                if (id != null) {
                    put.accept(id, dto);
                    if (id > max) max = id;
                }
            }
            if (max >= seq.get()) seq.set(max + 1);
        }
    }
}
