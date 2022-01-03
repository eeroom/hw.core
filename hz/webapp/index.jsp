<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="./bpmn-js/dist/assets/bpmn-font/css/bpmn.css">
    <link rel="stylesheet" href="./bpmn-js/dist/assets/diagram-js.css">
    <script src="./jquery/dist/jquery.js"></script>
    <script src="./bpmn-js/dist/bpmn-navigated-viewer.development.js"></script>
    <script >
        $(function (params) {
            var viewer=new BpmnJS({container:"#bpm"});
            $(document).on("click","#lstProc>li",function (params) {
                //发请求拿xml,已完成节点
                var defkey= $(this).data("defkey");
                var procid= $(this).data("procid");
                Promise.all([$.post("",{defkey}),$.post("",{procId})]).then(([xmlstr,lstActId])=>{
                    viewer.importXML(xmlstr)
                    .then(x=>{
                        var overlays=viewer.get("overlays");
                        var canvas=viewer.get("canvas");
                        var elementRegistry=viewer.get("elementRegistry");
                        //已完成的节点
                        lstActId.item1.forEach(nodeId => {
                            var shape=elementRegistry.get(nodeId);
                            var overlayHtml=$('<div class="highlight-task-completed"></div>').css({width:shape.width,height:shape.height});
                            overlays.add(nodeId,{
                                position:{top:0,left:0},
                                html:overlayHtml
                            });
                        });
                        //当前等待完成的节点
                        if(lstActId.item2){
                            var shape=elementRegistry.get(lstActId.item2);
                            var overlayHtml=$('<div class="highlight-task-cc"></div>').css({width:shape.width,height:shape.height});
                            overlays.add(lstActId.item2,{
                                position:{top:0,left:0},
                                html:overlayHtml
                            });
                        }
                    })
                });
            });

            $.post("",{},function (res) {
                var lstli= $.map(res.data,(el,i)=>$(`<li data-defkey="${el.defkey}" data-procid="${el.procId}">${el.procId}</li>`));
                $("#lstProc").append(lstli);
            })
        })
    </script>
</head>
<body>
    <h3><a target="_blank" href="swagger-ui.html">swagger-ui.html</a><h3></h3>
    <ul id="lstProc">
        
    </ul>
    <div id="bpm">

    </div>
    
</body>
</html>
