package com.a.b.c.d.pangolin.util;

import com.a.b.c.d.pangolin.util.bean.EntityTypeDTO;
import com.a.b.c.d.pangolin.util.bean.FreemarkerEntityParamDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FreemarkerUtil {
    private FreemarkerUtil() {
    }

    public static final String TPL_JAVA_ENTITY = "java_entity.ftl";
    public static final String TPL_CSHARP_ENTITY = "csharp_entity.ftl";

    public static List<EntityTypeDTO> buildJavaEntityTypeList(Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) {
            return Lists.newArrayList();
        }

        List<EntityTypeDTO> list = new ArrayList<EntityTypeDTO>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Objects.isNull(entry)) {
                continue;
            }
            String name = StringUtil.toLowerCamelCase(entry.getKey());
            if (Objects.isNull(entry.getValue())) {
                String type = "String";
                list.add(new EntityTypeDTO(type, name));
            } else {
                String type = entry.getValue().getClass().getSimpleName();
                list.add(new EntityTypeDTO(type, name));
            }
        }

        return list;
    }

    public static List<EntityTypeDTO> buildCSharpEntityTypeList(Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) {
            return Lists.newArrayList();
        }

        List<EntityTypeDTO> list = new ArrayList<EntityTypeDTO>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Objects.isNull(entry)) {
                continue;
            }
            String name = StringUtil.toUpperCamelCase(entry.getKey());
            if (Objects.isNull(entry.getValue())) {
                list.add(new EntityTypeDTO("string", name));
            } else if (entry.getValue().getClass() == Integer.class) {
                list.add(new EntityTypeDTO("int?", name));
            } else if (entry.getValue().getClass() == BigDecimal.class) {
                list.add(new EntityTypeDTO("decimal?", name));
            } else {
                list.add(new EntityTypeDTO("string", name));
            }
        }

        return list;
    }

    /**
     * 生成Entity代码
     *
     * @param tplName 例如: hello.ftl
     * @return
     * @throws IOException
     */
    public static String toEntityString(FreemarkerEntityParamDTO param, String tplName) throws Exception {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration(Configuration.getVersion());
        // step2：设置模板文件所在的路径
        configuration.setClassForTemplateLoading(FreemarkerUtil.class, "/ftl");
        // step3：设置模板文件使用的字符集。一般就是utf-8
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // step4：加载一个模板，创建一个模板对象
        Template template = configuration.getTemplate(tplName);

        // 输出为字符串
        try (StringWriter writer = new StringWriter();) {
            template.process(param, writer);
            return writer.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}