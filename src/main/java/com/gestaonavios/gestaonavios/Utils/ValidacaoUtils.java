package Utils;

import java.time.LocalDate;

public class ValidacaoUtils {

    private ValidacaoUtils() {}

    /**
     * Lança exceção se o valor for nulo ou em branco.
     * Exemplo: exigirTexto(navio.getNome(), "O nome do navio");
     * → "O nome do navio é obrigatório."
     */
    public static void exigirTexto(String valor, String nomeCampo) throws Exception {
        if (valor == null || valor.isBlank())
            throw new Exception(nomeCampo + " é obrigatório.");
    }

    /**
     * Lança exceção se o objeto for nulo.
     * Exemplo: exigirObjeto(carga.getTipoCarga(), "O tipo de carga");
     */
    public static void exigirObjeto(Object valor, String nomeCampo) throws Exception {
        if (valor == null)
            throw new Exception(nomeCampo + " é obrigatório.");
    }

    /**
     * Lança exceção se o valor não for positivo (> 0).
     * Exemplo: exigirPositivo(navio.getCapacidadeMaxima(), "A capacidade máxima");
     */
    public static void exigirPositivo(double valor, String nomeCampo) throws Exception {
        if (valor <= 0)
            throw new Exception(nomeCampo + " tem de ser um valor positivo.");
    }

    /**
     * Lança exceção se o valor inteiro não for positivo (> 0).
     */
    public static void exigirPositivo(int valor, String nomeCampo) throws Exception {
        if (valor <= 0)
            throw new Exception(nomeCampo + " tem de ser um valor positivo.");
    }

    /**
     * Lança exceção se o objeto for nulo, com mensagem "X não encontrado (id=N)".
     * Exemplo: exigirExistencia(navioDAL.buscarPorId(id), "Navio", id);
     */
    public static void exigirExistencia(Object objeto, String nomeEntidade, int id) throws Exception {
        if (objeto == null)
            throw new Exception(nomeEntidade + " não encontrado (id=" + id + ").");
    }

    /** NIF: exatamente 9 dígitos numéricos. */
    public static void exigirFormatoNif(String nif) throws Exception {
        if (nif == null || nif.length() != 9)
            throw new Exception("NIF inválido — deve ter exatamente 9 dígitos.");
        for (int i = 0; i < nif.length(); i++) {
            if (!Character.isDigit(nif.charAt(i)))
                throw new Exception("NIF inválido — deve conter apenas dígitos numéricos.");
        }
    }

    /** UNLOCODE: 2 letras maiúsculas (país) + 3 caracteres alfanuméricos maiúsculos. Ex: PTLEI */
    public static void exigirFormatoUnlocode(String locode) throws Exception {
        if (locode == null || locode.length() != 5)
            throw new Exception("Código UNLOCODE inválido — deve ter exatamente 5 caracteres (ex: PTLEI).");
        for (int i = 0; i < 2; i++) {
            if (!Character.isLetter(locode.charAt(i)) || !Character.isUpperCase(locode.charAt(i)))
                throw new Exception("Código UNLOCODE inválido — os primeiros 2 caracteres devem ser letras maiúsculas (código do país).");
        }
        for (int i = 2; i < 5; i++) {
            char c = locode.charAt(i);
            if (!Character.isLetterOrDigit(c) || !Character.isUpperCase(c) && !Character.isDigit(c))
                throw new Exception("Código UNLOCODE inválido — os últimos 3 caracteres devem ser alfanuméricos maiúsculos.");
        }
    }

    /** Ano de fabrico: entre 1900 e o ano corrente. */
    public static void exigirAnoFabrico(int ano) throws Exception {
        int anoAtual = LocalDate.now().getYear();
        if (ano < 1900 || ano > anoAtual)
            throw new Exception("Ano de fabrico inválido — deve estar entre 1900 e " + anoAtual + ".");
    }

    /**
     * Código IMO: aceita "1234567" (7 dígitos) ou "IMO1234567" (prefixo + 7 dígitos).
     * Exemplo: exigirFormatoIMO("IMO1234567");
     */
    public static void exigirFormatoIMO(String imo) throws Exception {
        if (imo == null)
            throw new Exception("Código IMO inválido — não pode ser nulo.");
        String digitos = imo.toUpperCase().startsWith("IMO") ? imo.substring(3) : imo;
        if (digitos.length() != 7)
            throw new Exception("Código IMO inválido — deve ter 7 dígitos numéricos (ex: IMO1234567 ou 1234567).");
        for (int i = 0; i < digitos.length(); i++) {
            if (!Character.isDigit(digitos.charAt(i)))
                throw new Exception("Código IMO inválido — os 7 dígitos devem ser numéricos.");
        }
    }
}
