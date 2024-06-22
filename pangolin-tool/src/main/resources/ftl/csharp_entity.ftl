using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

[Serializable]
public class CSharpEntity {
    <#list paramList as it>
    public ${it.type} ${it.name} {get; set;}
    </#list>
}