package br.com.fabulio.Fabulio.service;

public interface IConverteDados {
    <T> T obterDados(String Json, Class<T> classe);
}
