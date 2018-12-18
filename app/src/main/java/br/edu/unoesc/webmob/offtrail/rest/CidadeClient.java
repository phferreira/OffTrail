package br.edu.unoesc.webmob.offtrail.rest;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(converters = {MappingJackson2HttpMessageConverter.class})
public interface CidadeClient {
    @Get("https://viacep.com.br/ws/SC/{nomeCidade}/json")
    public List<Endereco> getEndereco(@Path String nomeCidade);

}
