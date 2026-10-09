package br.gov.anvisa.portfolio.infrastructure.persistence.fake;

import br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Profile("dev")
public class FakeDataStore {
    public final List<FakeCatalogoDto> catalogos = new ArrayList<>();
    public final Map<Long, FakeSistemaDto> sistemas = new ConcurrentHashMap<>();
    public final Map<Long, FakeServidorDto> servidores = new ConcurrentHashMap<>();
    public final Map<Long, FakeUrlDto> urls = new ConcurrentHashMap<>();
    public final Map<Long, FakeComponenteDto> componentes = new ConcurrentHashMap<>();
    public final Map<Long, FakeDocumentacaoDto> documentacoes = new ConcurrentHashMap<>();
    public final Map<Long, FakeRotinaDto> rotinas = new ConcurrentHashMap<>();
    public final Map<Long, FakeBackupDto> backups = new ConcurrentHashMap<>();
    public final Map<Long, FakeUsuarioDto> usuarios = new ConcurrentHashMap<>();
    public final Map<Long, FakeVinculoDto> vinculos = new ConcurrentHashMap<>();

    public final AtomicLong sistemaSeq = new AtomicLong(100);
    public final AtomicLong servidorSeq = new AtomicLong(100);
    public final AtomicLong urlSeq = new AtomicLong(100);
    public final AtomicLong componenteSeq = new AtomicLong(100);
    public final AtomicLong documentacaoSeq = new AtomicLong(100);
    public final AtomicLong rotinaSeq = new AtomicLong(100);
    public final AtomicLong backupSeq = new AtomicLong(100);
    public final AtomicLong usuarioSeq = new AtomicLong(100);
    public final AtomicLong vinculoSeq = new AtomicLong(1000);
}
