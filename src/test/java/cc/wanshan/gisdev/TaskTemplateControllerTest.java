package cc.wanshan.gisdev;

import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplate;
import cc.wanshan.gisdev.entity.TasktTemplate.TaskTemplateNode;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GisDevApplication.class)
@WebAppConfiguration
public class TaskTemplateControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreate() {

        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setTaskTemplateName("任务模板1");
        taskTemplate.setCreateTime(new Date(System.currentTimeMillis()));

        List<TaskTemplateNode> taskTemplateNodeList = Lists.newArrayList();

        TaskTemplateNode taskTemplateNode1 = new TaskTemplateNode();
        taskTemplateNode1.setTaskTemplateNodeName("任务节点1：步骤1");
        TaskTemplateNode taskTemplateNode2 = new TaskTemplateNode();
        taskTemplateNode2.setTaskTemplateNodeName("任务节点2：步骤2");

        taskTemplateNodeList.add(taskTemplateNode1);
        taskTemplateNodeList.add(taskTemplateNode2);

        taskTemplate.setTaskTemplateNodeList(taskTemplateNodeList);

        String jsonStr = JSONObject.toJSONString(taskTemplate);

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders.post("/rest/task/template")
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(jsonStr))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAll() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/rest/task/template"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(0)))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
