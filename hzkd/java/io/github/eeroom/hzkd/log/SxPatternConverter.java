package io.github.eeroom.hzkd.log;


import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "SxPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "sx" })
public class SxPatternConverter extends LogEventPatternConverter {
    String opt;
    public SxPatternConverter(String opt){
        super("sx","sx");
        this.opt=opt;
    }

    //这个log4j2类库的插件机制约定的方法，
    public static SxPatternConverter newInstance(String[] options) {
        return new SxPatternConverter(options[0]);
    }
    @Override
    public void format(LogEvent logEvent, StringBuilder stringBuilder) {
        var parameter= logEvent.getMessage().getParameters();
        if(parameter==null||parameter.length<1)
            return;
        var obj=parameter[0];
        try {
            var fd= obj.getClass().getField(this.opt);
            if(fd==null)
                return;
            var value= fd.get(obj);
            stringBuilder.append(value);
        } catch (Throwable e) {
            throw  new RuntimeException("读取属性值发送异常",e);
        }
    }
}
