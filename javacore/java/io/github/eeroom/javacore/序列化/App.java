package io.github.eeroom.javacore.序列化;

public class App {
    public void regexMatch(){

        var origin="<div>我的</div>QQ是:12345 我<a>的电</img>话是:7777 我<style>的邮</script>箱是:ss@163.com";
        String[] holdTags={"a","img"};
        var regexstr= String.format("<(?!((/?\\s?%s)))[^>]+>",String.join("\\b)|(/?\\s?",holdTags));
        java.util.regex.Pattern p=java.util.regex.Pattern.compile(regexstr);
        var m=p.matcher(origin);
        while (m.find()){
            System.out.println(m.group());
            origin= origin.replace(m.group(),"");
        }
        System.out.println(origin);
    }
}
