package br.com.fabulio.Fabulio.model;

public enum Categoria {
    ACAO("Action", "acao"),
    AVENTURA("Adventure", "aventura"),
    ANIMACAO("Animation", "animacao"),
    BIOGRAFIA("Biography", "biografia"),
    COMEDIA("Comedy", "comedia"),
    CRIME("Crime", "crime"),
    DOCUMENTARIO("Documentary", "documentario"),
    DRAMA("Drama", "drama"),
    FAMILIA("Family", "familia"),
    FANTASIA("Fantasy", "fantasia"),
    HISTORIA("History", "historia"),
    TERROR("Horror", "terror"),
    MUSICAL("Musical", "musical"),
    MISTERIO("Mystery", "misterio"),
    ROMANCE("Romance", "romance"),
    FICCAO_CIENTIFICA("Sci-Fi", "ficcao-cientifica"),
    ESPORTE("Sport", "esporte"),
    MUSICA("Music", "musica"),
    SUSPENSE("Thriller", "suspense"),
    GUERRA("War", "guerra"),
    CURTA("Short", "curta"),
    FAROESTE("Western", "faroeste"),
    OUTROS("Other", "outros");

    private String categoriaOmdb;
    private String categoriaPortugues;

    private Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        System.out.println("Categoria não mapeada: " + text);
        return OUTROS;
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
