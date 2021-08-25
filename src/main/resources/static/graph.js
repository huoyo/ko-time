
function MethodGraph(canvas) {
    var o = new Object();
    o.background = canvas;
    o.threshold = 800;
    o.allNodes = new Map();
    o.allNodeEnds = new Map();
    o.createMethodNode = function (data, x, y) {
        let name = (data.name + data.value).replaceAll(".", "-");
        let node;
        if (o.allNodes.get(name) != undefined) {
            node = o.allNodes.get(name);
            return node;
        } else {
            let bkColor = '#5FB878';
            if (data.avgRunTime>o.threshold) {
                bkColor = '#e01c32';
            }
            let exceptionColor = '#636c6c';
            if (data.exceptionNum>0) {
                exceptionColor = '#c97534';
            }
            let $info = "\n" +
                "<ul id=\"" + name + "\"  class='node' style=\"z-index:1;position: absolute;background-color: #636c6c;list-style: none;box-shadow: 5px 1px 5px #888888;cursor:default;border-radius: 8px 8px 8px 8px\">\n" +
                "    <li class='nodeli' style=\"position:relative;background-color: "+bkColor+";padding-left:10px;padding-right:10px;border-radius:8px 8px 0px 0px;\"><a style=\"color: white;font-size: 12px;\">指标</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 9px\">方法：" + data.name + "</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 9px\">类型：" + data.methodType + "</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 9px\">平均耗时：" + data.avgRunTime + " ms</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 9px\">最大耗时：" + data.maxRunTime + " ms</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 9px\">最小耗时：" + data.minRunTime + " ms</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;padding-left:10px;padding-right:10px;border-bottom: 1px solid lightslategrey;background-color: "+exceptionColor+";border-radius:0px 0px 8px 8px;\"><a style=\"color: white;font-size: 9px;\">异常数目：" + data.exceptionNum + " 个</a></li>\n" +
                "</ul>";
            $('#layerDemo').append($info);
            $("#" + name).css({
                top: y - 55,
                left: x + 50
            }).show();
            node = document.getElementById(name);
            o.allNodes.set(name, node);
            return node;
        };
    };
    o.createMethodLink = function (startNode, endNode) {
        if (!o.allNodeEnds.has(startNode.getAttribute('id'))) {
            o.allNodeEnds.set(startNode.getAttribute('id'), new Array());
            let newList = o.allNodeEnds.get(startNode.getAttribute('id'));
            newList.push(endNode.getAttribute('id'));
            o.allNodeEnds.set(startNode.getAttribute('id'), newList);
        } else {
            let newList = o.allNodeEnds.get(startNode.getAttribute('id'));
            newList.push(endNode.getAttribute('id'));
            o.allNodeEnds.set(startNode.getAttribute('id'), newList);
        }
        if (!o.allNodeEnds.has(endNode.getAttribute('id'))) {
            o.allNodeEnds.set(endNode.getAttribute('id'), new Array());
            let newList = o.allNodeEnds.get(endNode.getAttribute('id'));
            newList.push(startNode.getAttribute('id'));
            o.allNodeEnds.set(endNode.getAttribute('id'), newList);
        } else {
            let newList = o.allNodeEnds.get(endNode.getAttribute('id'));
            newList.push(startNode.getAttribute('id'));
            o.allNodeEnds.set(endNode.getAttribute('id'), newList);
        };
        var y_start = Number($(startNode).css("top").replace("px", "")) + $(startNode).height() / 2;
        var x_start = Number($(startNode).css("left").replace("px", "")) + $(startNode).width() / 2;
        var y_end = Number($(endNode).css("top").replace("px", "")) + $(endNode).height() / 2;
        var x_end = Number($(endNode).css("left").replace("px", "")) + $(endNode).width() / 2;
        var lx = x_end - x_start;
        var ly = y_end - y_start;
        var length = Math.sqrt(lx * lx + ly * ly);
        var c = 360 * Math.atan2(ly, lx) / (2 * Math.PI);
        var midX = (x_end + x_start) / 2;
        var midY = (y_end + y_start) / 2;
        var deg = c <= -90 ? (360 + c) : c;
        $("#"+o.background).append("<div id='" + startNode.getAttribute("id") + endNode.getAttribute("id") + "re' class='line' style='position:absolute;background:green;height:1px;top:" + midY + "px;left:" + (midX - length / 2) + "px;width:" + length + "px;transform:rotate(" + deg + "deg);'></div>");

    };
    o.createSimMethodLink = function (startNode, endNode) {
        var y_start = Number($(startNode).css("top").replace("px", "")) + $(startNode).height() / 2;
        var x_start = Number($(startNode).css("left").replace("px", "")) + $(startNode).width() / 2;
        var y_end = Number($(endNode).css("top").replace("px", "")) + $(endNode).height() / 2;
        var x_end = Number($(endNode).css("left").replace("px", "")) + $(endNode).width() / 2;
        var lx = x_end - x_start;
        var ly = y_end - y_start;
        var length = Math.sqrt(lx * lx + ly * ly);
        var c = 360 * Math.atan2(ly, lx) / (2 * Math.PI);
        var midX = (x_end + x_start) / 2;
        var midY = (y_end + y_start) / 2;
        var deg = c <= -90 ? (360 + c) : c;
        $("#"+o.background).append("<div id='" + startNode.getAttribute("id") + endNode.getAttribute("id") + "re' class='line' style='position:absolute;background:green;height:1px;top:" + midY + "px;left:" + (midX - length / 2) + "px;width:" + length + "px;transform:rotate(" + deg + "deg);'></div>");

    };

    o.isDragNode = false;
    o.isDragBack = false;
    o.moveNode;
    o.moveNodeX = 0;
    o.moveNodeY = 0;
    o.l = 0;
    o.t = 0;
    document.getElementById(o.background).onmousedown = function (e) {
        o.moveNodeX = e.clientX;
        o.moveNodeY = e.clientY;
        let clssName = e.path[0].getAttribute('class');
        if (clssName == 'node') {
            o.moveNode = e.path[0];
            o.isDragNode = true;
            o.l = o.moveNode.offsetLeft;
            o.t = o.moveNode.offsetTop;
            o.moveNode.style.cursor = 'move';
        } else if (clssName == 'nodeli') {
            o.moveNode = e.path[1];
            o.isDragNode = true;
            o.l = o.moveNode.offsetLeft;
            o.t = o.moveNode.offsetTop;
            o.moveNode.style.cursor = 'move';
        }else{
            o.isDragBack = true;
        };
    };
    document.getElementById(o.background).onmousemove = function (e) {
        var nx = e.clientX;
        var ny = e.clientY;
        var nl = nx - (o.moveNodeX - o.l);
        var nt = ny - (o.moveNodeY - o.t);
        if (o.isDragNode == true) {
            o.moveNode.style.left = nl + 'px';
            o.moveNode.style.top = nt + 'px';
            let childrenId = o.allNodeEnds.get(o.moveNode.getAttribute('id'));
            for (let i = 0; i < childrenId.length; i++) {
                let endId = childrenId[i];
                let endNode = document.getElementById(endId);
                if (document.getElementById(o.moveNode.getAttribute('id') + endId + 're') != undefined) {
                    document.getElementById(o.moveNode.getAttribute('id') + endId + 're').remove();
                    o.createSimMethodLink(o.moveNode, endNode);
                };
                if (document.getElementById(endId + o.moveNode.getAttribute('id') + 're') != undefined) {
                    document.getElementById(endId + o.moveNode.getAttribute('id') + 're').remove();
                    o.createSimMethodLink(endNode, o.moveNode);
                };
            };
        }else if (o.isDragBack==true) {
            let nodeKeys = o.allNodes.keys();
            o.allNodes.forEach(function(value,nodeKey){
                let pmoveNode = document.getElementById(nodeKey);
                let nl = nx - o.moveNodeX ;
                let nt = ny - o.moveNodeY ;
                if (nl>10) {
                    pmoveNode.style.left = Number(pmoveNode.style.left.replace('px',''))+10 + 'px';
                }else if(nl<-10){
                    pmoveNode.style.left = Number(pmoveNode.style.left.replace('px',''))-10 + 'px';
                };
                if (nt>6) {
                    pmoveNode.style.top = Number(pmoveNode.style.top.replace('px',''))+10 + 'px';
                }else if (nt<-6){
                    pmoveNode.style.top = Number(pmoveNode.style.top.replace('px',''))-10 + 'px';
                };
                let childrenId = o.allNodeEnds.get(nodeKey);
                for (let i = 0; i < childrenId.length; i++) {
                    let endId = childrenId[i];
                    let endNode = document.getElementById(endId);
                    if (document.getElementById(pmoveNode.getAttribute('id') + endId + 're') != undefined) {
                        document.getElementById(pmoveNode.getAttribute('id') + endId + 're').remove();
                        o.createSimMethodLink(pmoveNode, endNode, '-');
                    };
                    if (document.getElementById(endId + pmoveNode.getAttribute('id') + 're') != undefined) {
                        document.getElementById(endId + pmoveNode.getAttribute('id') + 're').remove();
                        o.createSimMethodLink(endNode, pmoveNode, '-');
                    };
                };
            });

        };
    };
    document.getElementById(o.background).onmouseup = function (e) {
        o.isDragNode = false;
        o.isDragBack = false;
        if ((o.moveNode!=undefined) && (o.moveNode.hasOwnProperty("style")==true) && (o.moveNode.style.hasOwnProperty("cursor")==true)) {
            o.moveNode.style.cursor = 'default';
        };
    };
    return o;
}