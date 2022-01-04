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
    <style>
        .bpm-img{
          height: 800px;
            width: 400px;
            border:1px red solid;
        }
        .highlight-task-completed{
            background-color: greenyellow;
            opacity: 0.4;
            border-radius: 10px;
        }
        .highlight-task-cc{
            background-color: rgb(29, 26, 218);
            opacity: 0.4;
            border-radius: 10px;
        }
    </style>
    <script >
        $(function (params) {
            var viewer=new BpmnJS({container:"#bpm"});
            $(document).on("click","#lstProc>li>a",function (params) {
                console.log("click",this);
                //发请求拿xml,已完成节点
                var procid= $(this).data("procid");
                $.post("/camunda/getBpmnjsData",{procid},function (res){
                    viewer.importXML(res.data.item1)
                        .then(x=>{
                            var overlays=viewer.get("overlays");
                            var canvas=viewer.get("canvas");
                            canvas.zoom("fit-viewport");
                            var elementRegistry=viewer.get("elementRegistry");
                            //已完成的节点
                            (res.data.item2||[]).forEach(nodeId => {
                                var shape=elementRegistry.get(nodeId);
                                var overlayHtml=$('<div class="highlight-task-completed"></div>').css({width:shape.width,height:shape.height});
                                overlays.add(nodeId,{
                                    position:{top:0,left:0},
                                    html:overlayHtml
                                });
                            });
                            //当前等待完成的节点
                            (res.data.item3||[]).forEach(nodeId => {
                                var shape=elementRegistry.get(nodeId);
                                var overlayHtml=$('<div class="highlight-task-cc"></div>').css({width:shape.width,height:shape.height});
                                overlays.add(nodeId,{
                                    position:{top:0,left:0},
                                    html:overlayHtml
                                });
                             });
                        })
                })
            });

            $.post("/camunda/getBizdataEntities",{},function (res) {
                var lstli= $.map(res.data,(el,i)=>$(`<li><a data-procid="${el.processId}">${el.processId}</a></li>`));
                $("#lstProc").append(lstli);
            })
        })
    </script>
</head>
<body>
    <h3><a target="_blank" href="swagger-ui.html">swagger-ui.html</a><h3></h3>
    <ul id="lstProc">
        
    </ul>
    <div id="bpm" class="bpm-img">

    </div>
    
</body>
</html>
