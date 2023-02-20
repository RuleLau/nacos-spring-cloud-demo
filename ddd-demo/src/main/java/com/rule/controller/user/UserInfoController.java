package com.rule.controller.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
@Slf4j
public class UserInfoController {

   /* @Autowired
    private IUserInfoService userInfoService;

    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Response save(@RequestBody UserInfoVO userInfoVO) {
        try {
            userInfoService.save(userInfoVO);
        } catch (BizException e) {
            log.error("保存接口出现业务异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.BIZ_ERROR.getErrCode(), e.getMessage());
        } catch (Exception e) {
            log.error("保存接口出现系统异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.SYS_ERROR.getErrCode(), "保存异常");
        }
        return Response.buildSuccess();
    }

    @ResponseBody
    @RequestMapping(value = "del", method = RequestMethod.POST)
    public Response del(@RequestBody UserInfoOperationVO operationVo) {
        try {
            userInfoService.delete(operationVo.getIds());
        } catch (BizException e) {
            log.error("删除接口出现业务异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.BIZ_ERROR.getErrCode(), e.getMessage());
        } catch (Exception e) {
            log.error("删除接口出现系统异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.SYS_ERROR.getErrCode(), "删除失败");
        }

        return Response.buildSuccess();
    }

    @ResponseBody
    @RequestMapping(value = "status/update", method = RequestMethod.POST)
    public Response updateStatus(@RequestBody UserInfoOperationVO operationVo) {
        try {
            userInfoService.batchUpdateStatus(operationVo.getIds(), operationVo.getStatus());
        } catch (BizException e) {
            log.error("更新状态接口出现业务异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.BIZ_ERROR.getErrCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新状态接口出现系统异常，异常信息为", e);
            return Response.buildFailure(ErrorCode.SYS_ERROR.getErrCode(), "删除失败");
        }

        return Response.buildSuccess();
    }
*/
}
