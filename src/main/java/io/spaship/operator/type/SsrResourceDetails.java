package io.spaship.operator.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.spaship.operator.util.ReUsableItems;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record SsrResourceDetails
        (String nameSpace,
         String imageUrl,
         String app,
         String contextPath,
         String healthCheckPath,
         String website,
         String environment,
         String port,
         Map<String, String> configMap,
         Map<String, String> secretMap
        ) {

    private static final Logger LOG = LoggerFactory.getLogger(SsrResourceDetails.class);


    public Map<String, String> toTemplateParameterMap(){
        Map<String, String> params = new HashMap<>();

        if(Objects.nonNull(imageUrl))
            params.put("IMAGE-URL", imageUrl);
        if(Objects.nonNull(app))
            params.put("APP", app);
        if(Objects.nonNull(contextPath))
            params.put("CONTEXT-PATH", contextPath);
        if(Objects.nonNull(healthCheckPath))
            params.put("HEALTH-CHECK-PATH", healthCheckPath);
        if(Objects.nonNull(website))
            params.put("WEBSITE", website);
        if(Objects.nonNull(environment))
            params.put("ENV", environment);
        if(Objects.nonNull(port))
            params.put("CONPORT", port);

        var routerDomain = fetchRouterDomain();
        if(Objects.nonNull(routerDomain))
            params.put("ROUTER-DOMAIN", routerDomain);

        //TODO remove this code and introduce a new class for credentials, for handling private image repo
        params.put("IMAGE-PULL-SECRET-NAME", ReUsableItems.remoteBuildImageRepoSecretName());
        params.put("REPO-ACCESS-CREDS", ReUsableItems.remoteBuildImagePullSecret());

        LOG.debug("\n");
        LOG.debug("deployment parameters are as follows {}",params);
        LOG.debug("\n");
        return params;
    }

    public String fetchRouterDomain(){
        String routerDomainFromProperty = null;
        try{
            routerDomainFromProperty = ConfigProvider.getConfig().getValue("operator.domain.name", String.class);
        }catch(Exception e){
            LOG.error("failed to fetch the value of property operator.domain.name due to {}",e.getMessage());
        }
        return routerDomainFromProperty;
    }




}