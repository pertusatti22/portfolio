package br.gov.anvisa.portfolio.api.error;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Traducao das excecoes da aplicacao para respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String REQUISICAO_INVALIDA = "bad_request";
    private static final String RECURSO_NAO_ENCONTRADO = "not_found";
    private static final String REGRA_DE_NEGOCIO = "business_rule";
    private static final String CONFLITO = "conflict";
    private static final String ERRO_INTERNO = "internal_error";

    private final Clock clock;

    public ApiExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> tratarNaoEncontrado(RecursoNaoEncontradoException excecao,
                                                               HttpServletRequest requisicao) {
        return responder(HttpStatus.NOT_FOUND, RECURSO_NAO_ENCONTRADO, excecao.getMessage(), requisicao, List.of());
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ApiErrorResponse> tratarConflito(RegistroDuplicadoException excecao,
                                                            HttpServletRequest requisicao) {
        return responder(HttpStatus.CONFLICT, CONFLITO, excecao.getMessage(), requisicao, List.of());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ApiErrorResponse> tratarRegraDeNegocio(RegraDeNegocioException excecao,
                                                                 HttpServletRequest requisicao) {
        return responder(HttpStatus.UNPROCESSABLE_ENTITY, REGRA_DE_NEGOCIO, excecao.getMessage(), requisicao,
                List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> tratarValidacao(MethodArgumentNotValidException excecao,
                                                            HttpServletRequest requisicao) {
        List<ApiErrorResponse.CampoErro> campos = excecao.getBindingResult().getAllErrors().stream()
                .map(erro -> new ApiErrorResponse.CampoErro(campoDe(erro), erro.getDefaultMessage()))
                .toList();
        return responder(HttpStatus.BAD_REQUEST, REQUISICAO_INVALIDA, "Dados invalidos", requisicao, campos);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> tratarTipoInvalido(MethodArgumentTypeMismatchException excecao,
                                                               HttpServletRequest requisicao) {
        return responder(HttpStatus.BAD_REQUEST, REQUISICAO_INVALIDA,
                "Valor invalido para o parametro %s".formatted(excecao.getName()), requisicao, List.of());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> trataParametroObrigatorio(MissingServletRequestParameterException excecao,
                                                                     HttpServletRequest requisicao) {
        return responder(HttpStatus.BAD_REQUEST, REQUISICAO_INVALIDA,
                "Parametro obrigatorio ausente: %s".formatted(excecao.getParameterName()), requisicao, List.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> tratarArgumentoInvalido(IllegalArgumentException excecao,
                                                                  HttpServletRequest requisicao) {
        return responder(HttpStatus.BAD_REQUEST, REQUISICAO_INVALIDA, excecao.getMessage(), requisicao, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> tratarInesperado(Exception excecao, HttpServletRequest requisicao) {
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, ERRO_INTERNO, "Erro inesperado", requisicao, List.of());
    }

    private String campoDe(Object erro) {
        return erro instanceof FieldError campo ? campo.getField() : erro.toString();
    }

    private ResponseEntity<ApiErrorResponse> responder(HttpStatus status,
                                                      String erro,
                                                      String mensagem,
                                                      HttpServletRequest requisicao,
                                                      List<ApiErrorResponse.CampoErro> campos) {
        ApiErrorResponse corpo = new ApiErrorResponse(Instant.now(clock),
                status.value(),
                erro,
                mensagem,
                requisicao.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(corpo);
    }
}