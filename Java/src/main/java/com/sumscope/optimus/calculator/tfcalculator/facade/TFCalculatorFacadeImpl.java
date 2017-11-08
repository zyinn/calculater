package com.sumscope.optimus.calculator.tfcalculator.facade;

import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.calculator.shared.gatewayinvoke.CDHRepoRatesGateway;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.tfcalculator.facade.converter.ExcelExport;
import com.sumscope.optimus.calculator.tfcalculator.facade.converter.RequestDtoConverter;
import com.sumscope.optimus.calculator.tfcalculator.facade.converter.ResultDtoConverter;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.TFCalculatorQDPRestGateway;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPResponse;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.*;
import com.sumscope.optimus.calculator.tfcalculator.utils.ValidateUtils;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.facade.AbstractExceptionCatchedFacadeImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuejian.sun on 2016/7/27.
 */

@RestController
@RequestMapping(value = "/tfcalculator")
public class TFCalculatorFacadeImpl extends AbstractExceptionCatchedFacadeImpl implements TFCalculatorFacade{
    @Autowired
    private RequestDtoConverter requestDtoConverter;
    @Autowired
    private ExcelExport excelExport;
    @Autowired
    private CDHRepoRatesGateway cdhRepoRatesGateway;
    @Autowired
    private ResultDtoConverter resultDtoConverter;
    @Autowired
    private TFCalculatorQDPRestGateway tFCalculatorQDPRestGateway;

    @Override
    @RequestMapping(value="/tcInit",method = RequestMethod.POST)
    public void initPage(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            ValidateUtils.setResponseHeader(request, response);
            List<FutureContract> defaultFutureContract = requestDtoConverter.getDefaultFutureContract(); //默认的期货合约列表
            List<RepoRateDto> repoRateDtos = cdhRepoRatesGateway.retrieveRepoRates(); //拿到资金成本列表
            CalculationRequestForInit calculationRequestForInit = requestDtoConverter.convertInitialPageRequest(requestDtoConverter.getInitalFutureContract(defaultFutureContract),
                    requestDtoConverter.getInitalRepoRateDto(repoRateDtos));
            List<BondInfo> list=new ArrayList<BondInfo>();
            list.addAll(calculationRequestForInit.getSelectableBonds());
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(calculationRequestForInit);
            calculationRequestForInit.setSelectableBonds(list);
            IniResponseDto iniResponseDto = resultDtoConverter.convertInitResponseDto(calculationRequestForInit,tfCalculatorQDPResponse);
            return resultDtoConverter.convertInitialResponsesDto(iniResponseDto, defaultFutureContract, repoRateDtos);
        });
     
    }

    @Override
    @RequestMapping(value = "/doFutureInitCalculation",method = RequestMethod.POST)
    public void doFutureInitCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody String futureInit) {
        performWithExceptionCatch(httpResponse, () -> {
            ValidateUtils.setResponseHeader(httpRequest, httpResponse);
            CalculationRequestForInit calculationRequestForInit=requestDtoConverter.convertFutureInitRequestDto(futureInit);
            CalculationRequestForInit calculationRequestForInits = (CalculationRequestForInit)calculationRequestForInit.clone();
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(calculationRequestForInit);
            return resultDtoConverter.convertFutureInitResponseDto(tfCalculatorQDPResponse,calculationRequestForInits);
        });
    }

    @Override
    @RequestMapping(value = "/doGetFuturePriceCalculation",method = RequestMethod.POST)
    public void doGetFuturePriceCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody String futurePriceCalculation) {
        performWithExceptionCatch(httpResponse, () -> {
            ValidateUtils.setResponseHeader(httpRequest, httpResponse);
            CalculationRequest calculationRequest = requestDtoConverter.convertGetFuturePriceRequestDto(futurePriceCalculation);
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(calculationRequest);
            return resultDtoConverter.convertGetFuturePriceResponseDto(tfCalculatorQDPResponse, calculationRequest);
        });
    }

    @Override
    @RequestMapping(value = "/bondChanged",method = RequestMethod.POST)
    public void doBondChangedCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestBody String changBond) {
        performWithExceptionCatch(httpResponse, () -> {
            ValidateUtils.setResponseHeader(httpRequest, httpResponse);
            CalculationRequestWithBondYieldType calculation = requestDtoConverter.convertBondChangedRequestDto(changBond);
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(calculation);
            return  resultDtoConverter.convertBondChangedResponseDto(tfCalculatorQDPResponse,calculation);
        });
    }

    @Override
    @RequestMapping(value = "/doGetBondYieldCalculation",method = RequestMethod.POST)
    public void doGetBondYieldCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody String bondYield) {
        performWithExceptionCatch(httpResponse, () -> {
            ValidateUtils.setResponseHeader(httpRequest, httpResponse);
            CalculationRequestWithBondYieldType bondYieldRequest = requestDtoConverter.convertGetYieldRequestDto(bondYield);
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(bondYieldRequest);
            return  resultDtoConverter.convertGetYieldResponseDto(tfCalculatorQDPResponse, bondYieldRequest);
        });
    }

    @Override
    @RequestMapping(value = "/doCalculation",method = RequestMethod.POST)
    public void doCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestBody String doCalculation) {
        performWithExceptionCatch(httpResponse, () -> {
            ValidateUtils.setResponseHeader(httpRequest, httpResponse);
            CalculationRequest calculationRequest=requestDtoConverter.convertCalculationRequestDto(doCalculation);
            TFCalculatorQDPResponse tfCalculatorQDPResponse = tFCalculatorQDPRestGateway.doCalculation(calculationRequest);
            return resultDtoConverter.convertCalculationResponseDto(tfCalculatorQDPResponse,calculationRequest ,calculationRequest.getYieldTypeEnum());
        });
    }

    @Override
    @RequestMapping(value = "/retrieveRepoRates",method = RequestMethod.POST)
    public void retrieveRepoRates(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            ValidateUtils.setResponseHeader(request, response);
            return cdhRepoRatesGateway.retrieveRepoRates(); //拿到资金成本列表
        });
    }

    @Override
    @RequestMapping(value = "/findFixInterestBondsByPrefix",method = RequestMethod.POST)
    public void findFixInterestBondsByPrefix(HttpServletRequest request, HttpServletResponse response,@RequestBody String inputParams) {
        performWithExceptionCatch(response, () -> {
            ValidateUtils.setResponseHeader(request, response);
            return requestDtoConverter.getBondInfosByUserInputParams(inputParams);
        });
    }

    @Override
    @RequestMapping(value = "/excelExport",method = RequestMethod.POST)
    public void excelExport(HttpServletRequest request, HttpServletResponse response, @RequestBody List<CalculationResult> calculationResults) {
        performWithExceptionCatch(response, () -> {
            ValidateUtils.setResponseHeader(request, response);
            return excelExport.excelExport(request, calculationResults);
        });
    }

    @RequestMapping(value = "/excelExports",method = RequestMethod.POST)
    public void excelExports(HttpServletRequest request, HttpServletResponse response,@RequestBody List<CalculationResult> calculationResults) throws IOException {
        ValidateUtils.setResponseHeader(request, response);
        if(calculationResults==null || calculationResults.size()<0){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"导出失败,请稍后再试或者联系管理员");
        }
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment");
        response.setHeader("filename","excel.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        HSSFWorkbook hssfWorkbook = excelExport.getHssfWorkbook(calculationResults);
        hssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    //OPTIONS请求
    @RequestMapping(value = "/doFutureInitCalculation", method = RequestMethod.OPTIONS)
    public void doFutureInitCalculationOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/tcInit", method = RequestMethod.OPTIONS)
    public void initPageOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/doGetFuturePriceCalculation", method = RequestMethod.OPTIONS)
    public void doGetFuturePriceCalculationOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/doGetBondYieldCalculation", method = RequestMethod.OPTIONS)
    public void doGetBondYieldCalculationOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/doCalculation", method = RequestMethod.OPTIONS)
    public void doCalculationOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/bondChanged", method = RequestMethod.OPTIONS)
    public void doBondChangedCalculationOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/findFixInterestBondsByPrefix", method = RequestMethod.OPTIONS)
    public void findFixInterestBondsByPrefixOptions(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/excelExport", method = RequestMethod.OPTIONS)
    public void excelExport(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
    @RequestMapping(value = "/excelExports", method = RequestMethod.OPTIONS)
    public void excelExports(HttpServletRequest request, HttpServletResponse response) {
        ValidateUtils.getMethodOptions(request, response);
    }
}
