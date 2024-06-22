import java.io.Serializable;
import java.math.BigDecimal;

public class JavaEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    <#list paramList as it>
    private ${it.type} ${it.name};
    </#list>
}