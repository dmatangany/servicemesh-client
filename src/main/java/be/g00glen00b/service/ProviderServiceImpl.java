package be.g00glen00b.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import be.g00glen00b.dto.ProviderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProviderServiceImpl {
    @Value("${resource.providers}")
    private String resource;
    @Value("${resource.providers}/{id}")
    private String idResource;
    @Value("${resource.providers}/alert")
    private String alertResource;
    @Value("${resource.providers}/send")
    private String sendResource;
    @Autowired
    private RestTemplate restTemplate;

    public List<ProviderDTO> findAll() {
        return Arrays.stream(restTemplate.getForObject(resource, ProviderDTO[].class)).collect(Collectors.toList());
    }

    public ProviderDTO alertAll() {
        return restTemplate.getForObject(alertResource, ProviderDTO.class);
    }

    public ProviderDTO update(Long id, ProviderDTO provider) {
        return restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(provider), ProviderDTO.class, id).getBody();
    }

    public void delete(Long id) {
        restTemplate.delete(idResource, id);
    }

    public ProviderDTO create(ProviderDTO provider) {
        return restTemplate.postForObject(resource, provider, ProviderDTO.class);
    }

    public ProviderDTO send(ProviderDTO provider) {
        return restTemplate.postForObject(sendResource, provider, ProviderDTO.class);
    }
}
