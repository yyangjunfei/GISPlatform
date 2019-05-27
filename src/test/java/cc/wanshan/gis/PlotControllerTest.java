package cc.wanshan.gis;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GISPlatformApplication.class)
@WebAppConfiguration
public class PlotControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 绘制点保存测试
     * <p>
     * 绘制线保存测试
     * <p>
     * 绘制面保存测试
     * <p>
     * 根据类型查询测试
     */

    @Test
    public void testCreatePoint() {
        String jsonStr =
                "{"
                        + "\"name\":\"标注点\","
                        + "\"type\":\"Point\","
                        + "\"style\":\"样式\","
                        + "\"color\":\"颜色\","
                        + "\"geom\":{\"type\":\"Point\",\"coordinates\":[109.4897800000,36.5852900000]}"
                        + "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders.post("/rest/plot")
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

    /**
     * 绘制线保存测试
     */

    @Test
    public void testCreateLineString() {
        String jsonStr =
                "{"
                        + "\"name\":\"标注线\","
                        + "\"type\":\"LineString\","
                        + "\"style\":\"样式\","
                        + "\"color\":\"颜色\","
                        + "\"geometry\":{\"type\":\"Feature\",\n" +
                        "    \"properties\":{},\n" +
                        "    \"geometry\":{\n" +
                        "        \"type\":\"LineString\",\n" +
                        "        \"coordinates\":[[105.6005859375,30.65681556429287],\n" +
                        "        [107.95166015624999,31.98944183792288],\n" +
                        "        [109.3798828125,30.031055426540206],\n" +
                        "        [107.7978515625,29.935895213372444]]\n" +
                        "        }\n" +
                        "    }"
                        + "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders.post("/rest/plot")
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

    /**
     * 绘制面保存测试
     */

    @Test
    public void testCreatePolygon() {
        String jsonStr =
                "{"
                        + "\"name\":\"标注面\","
                        + "\"type\":\"Polygon\","
                        + "\"style\":\"样式\","
                        + "\"color\":\"颜色\","
                        + "\"geometry\":{\n" +
                        "        \"type\":\"Polygon\",\n" +
                        "        \"coordinates\":[\n" +
                        "                        [\n" +
                        "                          [106.10595703125,33.33970700424026],\n" +
                        "                          [106.32568359375,32.41706632846282],\n" +
                        "                          [108.03955078125,32.2313896627376],\n" +
                        "                          [108.25927734375,33.15594830078649],\n" +
                        "                          [106.10595703125,33.33970700424026]\n" +
                        "                        ]\n" +
                        "                      ]\n" +
                        "        }"
                        + "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders.post("/rest/plot")
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

    /**
     * 根据类型查询测试
     */

    @Test
    public void selectByTypeTest() {

        String type = "Point";
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/rest/plot/findAll")
                    .param("type", type)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            int statusCode = result.getResponse().getStatus();
            Assert.assertEquals(statusCode, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
