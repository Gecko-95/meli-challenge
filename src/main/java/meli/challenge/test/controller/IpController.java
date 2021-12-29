package meli.challenge.test.controller;


import io.swagger.annotations.ApiOperation;
import meli.challenge.test.model.CountryInfoComplete;
import meli.challenge.test.model.Statistics;
import meli.challenge.test.model.StatisticsDTO;
import meli.challenge.test.service.IpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IpController {

    private IpService ipService;

    public IpController(IpService ipService) {
        this.ipService = ipService;
    }

    @ApiOperation(value = "Limpia todas las caches de memoria")
    @RequestMapping(value = "/internal/clearMemoryCache", method = RequestMethod.POST, produces = "application/json")
    public void clearCache() {
        ipService.clearCache();
    }

    @ApiOperation(value = "Retorna un Hello World! :D")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloWorld() {
        return "Hello World!";
    }

    @ApiOperation(value = "Retorna las estadisticas")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
    public StatisticsDTO getStatistics() {
        return ipService.getStatisticsObjectByEndpoint().orElse(null);
    }

    @ApiOperation(value = "Retorna información completa de las ip")
    @RequestMapping(value = "/country/info", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CountryInfoComplete countryInfoComplete(@RequestParam String ip) {
        return ipService.countryInfoComplete(ip);
    }

    @ApiOperation(value = "Bloquea las ip")
    @RequestMapping(value = "/blacklist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CountryInfoComplete blackList(@RequestParam String ip) {
        return ipService.blackList(ip);
    }

    @ApiOperation(value = "Retorna todas los country info de las ip persistidas")
    @RequestMapping(value = "/ips/saved", method = RequestMethod.GET, produces = "application/json")
    public List<Statistics> getipsSaved() {
        return ipService.getStatisticsssObjectByEndpoint();
    }
}
