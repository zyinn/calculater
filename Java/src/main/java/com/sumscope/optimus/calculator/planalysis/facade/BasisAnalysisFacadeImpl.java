package com.sumscope.optimus.calculator.planalysis.facade;

import com.sumscope.optimus.commons.facade.AbstractExceptionCatchedFacadeImpl;
import com.sumscope.optimus.commons.util.JsonUtil;
import com.sumscope.optimus.calculator.planalysis.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.RequestDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.ResponseDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.ResponseInititialBasisDto;
import com.sumscope.optimus.calculator.planalysis.service.BasisAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by simon.mao on 2016/4/26.
 * Facade层实现类
 */
@RestController
@RequestMapping(value = "/tf/calculator")
public class BasisAnalysisFacadeImpl extends AbstractExceptionCatchedFacadeImpl implements BasisAnalysisFacade {

    @Autowired
    private BasisAnalysisService basisAnalysisService;

    @Override
    @RequestMapping(value = "/initial")
    public void initialRequest(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response,()->{
            setResponseHeader(request, response);
            ResponseInititialBasisDto responseInititialBasisDto = basisAnalysisService.inititialRequest(null);
            return responseInititialBasisDto;
        });

    }

    @Override
    @RequestMapping(value = "/initial/tfId")
    public void initialRequestWithTfId(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            setResponseHeader(request, response);
            ResponseInititialBasisDto responseInititialBasisDto = basisAnalysisService.inititialRequest(params);
            return responseInititialBasisDto;
        });

    }

    private ResponseDto performFutrueContactChange(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
        RequestDto requestDto = JsonUtil.readValue(params, RequestDto.class);
        ResponseDto responseDto = basisAnalysisService.futureContractPriceUpdate(requestDto);
        return responseDto;
    }

    @Override
    @RequestMapping(value = "/futurePrice")
    public void futurePriceUpdate(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response,()-> performFutrueContactChange(params, request, response));

    }

    @Override
    @RequestMapping(value = "/yieldType")
    public void yieldTypeUpdate(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            setResponseHeader(request, response);
            RequestDto requestDto = JsonUtil.readValue(params, RequestDto.class);
            ResponseDto responseDto = basisAnalysisService.yieldTypeUpdate(requestDto);
            return responseDto;
        });
    }

    @Override
    @RequestMapping(value = "/param")
    public void paramUpdate(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("params:            " + params);
        performWithExceptionCatch(response,()->{
            setResponseHeader(request, response);
            RequestDto requestDto = JsonUtil.readValue(params, RequestDto.class);
            ResponseDto responseDto = basisAnalysisService.paramUpdate(requestDto);
            return responseDto;
        });

    }

    @Override
    @RequestMapping(value = "/bond")
    public void findBondsByNamePrefix(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            setResponseHeader(request, response);
            List<BondPrimaryKeyDto> bondsByNamePrifix = basisAnalysisService.findBondsByNamePrifix(params);
            return bondsByNamePrifix;
        });

    }

    private void setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
    }

    /**
     * 为了支持跨域访问而制作的方法,GET请求时会先行OPTIONS请求，不能带参数
     */
    @RequestMapping(value = "/initial/tfId", method = RequestMethod.OPTIONS)
    public void initialRequestOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }

    @RequestMapping(value = "/futureContract", method = RequestMethod.OPTIONS)
    public void futureContractUpdateOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }

    @RequestMapping(value = "/futurePrice", method = RequestMethod.OPTIONS)
    public void futurePriceUpdateOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }

    @RequestMapping(value = "/yieldType", method = RequestMethod.OPTIONS)
    public void yieldTypeUpdateOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }

    @RequestMapping(value = "/param", method = RequestMethod.OPTIONS)
    public void paramUpdateOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }

    @RequestMapping(value = "/bond", method = RequestMethod.OPTIONS)
    public void findBondsByNamePrefixOptions(HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(request, response);
    }
}
