package com.kosta.common.spring.ctr;


import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.ctx.StatusContext;
import com.kosta.common.core.module.srv.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("sync")
@RequestMapping("/api")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService){
        this.statusService = statusService;
    }

    @GetMapping("/status")
    public StatusContext getStatus(){

        return statusService.getCurrentStatus();
    }
}
