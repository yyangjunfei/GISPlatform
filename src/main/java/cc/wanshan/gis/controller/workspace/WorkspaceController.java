package cc.wanshan.gis.controller.workspace;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/***
 * @author ：Yang
 * @date ：2019-7-23
 */

@Api(value = "WorkspaceController", tags = "工作空间接口")
@Controller
@ResponseBody
@RequestMapping(value = "/workspace")
public class WorkspaceController {

    private static Logger LOG = LoggerFactory.getLogger(WorkspaceController.class);

    @ApiOperation(value = "获取工作空间名称",httpMethod = "GET",notes = "获取工作空间名称")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Result getWorkspaceNames(){
        //获取工作空间名
        List<String> workspaceNames = GeoServerUtils.manager.getReader().getWorkspaceNames();
        return ResultUtil.success(workspaceNames);
    }

    /**
     * workspaceName The name of the workspace.
     * recurse The recurse parameter is used to recursively delete all resources contained by the specified workspace. This includes data
     *stores, coverage stores, feature types, etc...
     * @param workspaceName
     * @param recurse
     * @return true if the WorkSpace was successfully removed.
     */

    @ApiOperation(value = "删除工作空间名称",httpMethod = "DELETE",notes = "删除工作空间名称")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result deleteWorkspaceNames(String workspaceName,Boolean recurse ){
        //删除工作空间名
        Boolean  removeFlag = GeoServerUtils.publisher.removeWorkspace(workspaceName,recurse);
        if (removeFlag){
            return ResultUtil.success("删除工作空间成功！");
        }else{
            return ResultUtil.success("删除工作空间失败！");
        }
    }
}
