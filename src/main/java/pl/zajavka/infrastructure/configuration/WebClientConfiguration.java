package pl.zajavka.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.zajavka.infrastructure.cepik.ApiClient;
import pl.zajavka.infrastructure.cepik.api.PojazdyApi;
import pl.zajavka.infrastructure.cepik.api.SownikiApi;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Value("${api.cepik.url}")
    private String cepikUrl;

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {        // kazde api musi miec oddzielnych webClientow
        final var strategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    configurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public ApiClient apiClient(final WebClient webClient){
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(cepikUrl);
        return apiClient;
    }

    @Bean
    public PojazdyApi pojazdyApi(final ApiClient apiClient){
        return new PojazdyApi(apiClient);
    }

    @Bean
    public SownikiApi sownikiApi(final ApiClient apiClient){
        return new SownikiApi(apiClient);
    }
}
