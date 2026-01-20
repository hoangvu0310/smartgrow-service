package com.hoang.jobfinder;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.service.SocialAuthService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SocialAuthRouter {
  private final Map<Enum.AuthType, SocialAuthService> serviceMap;

  public SocialAuthRouter(List<SocialAuthService> services) {
    this.serviceMap = services.stream()
        .collect(Collectors.toMap(
            SocialAuthService::authType,
            Function.identity()
        ));
  }

  public SocialAuthService get(Enum.AuthType provider) {
    SocialAuthService service = serviceMap.get(provider);
    if (service == null) {
      throw new IllegalArgumentException("Unsupported provider: " + provider);
    }
    return service;
  }
}
