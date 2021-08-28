
function MethodGraph(canvas,timeThreshold) {
    var o = new Object();
    o.background = canvas;
    o.threshold = timeThreshold;
    o.allNodes = new Map();
    o.allNodeStarts = new Map();
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
                "<ul id=\"" + name + "\"  class='node uk-list' style=\"z-index:1;position: absolute;background-color: #636c6c;list-style: none;box-shadow: 5px 1px 5px #888888;cursor:default;border-radius: 8px 8px 8px 8px\">\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:18px;background-color: "+bkColor+";padding-left:10px;padding-right:10px;border-radius:8px 8px 0px 0px;\"><font style=\"color: white;font-size: 12px;\">指标</font></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:6px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><a style=\"color: white;font-size: 8px\">方法：" + data.name + "</a></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:6px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><font style=\"color: white;font-size: 8px\">类型：" + data.methodType + "</font></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:6px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><font style=\"color: white;font-size: 8px\">平均耗时：" + data.avgRunTime + " ms</font></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:6px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><font style=\"color: white;font-size: 8px\">最大耗时：" + data.maxRunTime + " ms</font></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:6px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: #636c6c\"><font style=\"color: white;font-size: 8px\">最小耗时：" + data.minRunTime + " ms</font></li>\n" +
                "    <li class='nodeli' style=\"position:relative;line-height:8px;padding-left:10px;padding-right:10px;padding-bottom:7px;border-bottom: 1px solid lightslategrey;background-color: "+exceptionColor+";border-radius:0px 0px 8px 8px;\"><font style=\"color: white;font-size: 8px;\">异常数目：" + data.exceptionNum + " 个</font></li>\n" +
                "</ul>";
            $('#'+o.background).append($info);
            $("#" + name).css({
                top: y ,
                left: x
            }).show();
            node = document.getElementById(name);
            o.allNodes.set(name, node);
            return node;
        };
    };
    o.createMethodLink = function (startNode, endNode) {
        if (!o.allNodeStarts.has(startNode.getAttribute('id'))) {
            o.allNodeStarts.set(startNode.getAttribute('id'), new Array());
            let newList = o.allNodeStarts.get(startNode.getAttribute('id'));
            newList.push(endNode.getAttribute('id'));
            o.allNodeStarts.set(startNode.getAttribute('id'), newList);
        } else {
            let newList = o.allNodeStarts.get(startNode.getAttribute('id'));
            newList.push(endNode.getAttribute('id'));
            o.allNodeStarts.set(startNode.getAttribute('id'), newList);
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
        $("#"+o.background).append("<div id='" + startNode.getAttribute("id") + endNode.getAttribute("id") + "reArrow' class='line' style='z-index:-3;position:absolute;top:" + (y_end-5) + "px;left:" + (x_end-5) + "px;width:" + length + "px;transform:rotate(" + deg + "deg);width: 0;height: 0;border: 6px solid transparent;border-left-color: green;border-right: none;border-top-color: transparent;border-bottom-color: transparent;'></div>");

    };
    o.createSimMethodLink = function (startNode, endNode) {
        var y_start = Number($(startNode).css("top").replace("px", "")) + $(startNode).height() / 2;
        var x_start = Number($(startNode).css("left").replace("px", "")) + $(startNode).width() ;
        var y_end = Number($(endNode).css("top").replace("px", "")) + $(endNode).height() / 2;
        var x_end = Number($(endNode).css("left").replace("px", "")) ;
        var lx = x_end - x_start;
        var ly = y_end - y_start;
        var length = Math.sqrt(lx * lx + ly * ly);
        var c = 360 * Math.atan2(ly, lx) / (2 * Math.PI);
        var midX = (x_end + x_start) / 2;
        var midY = (y_end + y_start) / 2;
        var deg = c <= -90 ? (360 + c) : c;
        $("#"+o.background).append("<div id='" + startNode.getAttribute("id") + endNode.getAttribute("id") + "re' class='line' style='z-index:-2;position:absolute;background:green;height:1px;top:" + midY + "px;left:" + (midX - length / 2) + "px;width:" + length + "px;transform:rotate(" + deg + "deg);'></div>");
        $("#"+o.background).append("<div id='" + startNode.getAttribute("id") + endNode.getAttribute("id") + "reArrow' class='line' style='z-index:-3;position:absolute;top:" + (y_end-5) + "px;left:" + (x_end-5) + "px;width:" + length + "px;transform:rotate(" + deg + "deg);width: 0;height: 0;border: 6px solid transparent;border-left-color: green;border-right: none;border-top-color: transparent;border-bottom-color: transparent;'></div>");
    };

    o.reDrawLines = function(moveNode){
        let childrenId = o.allNodeStarts.get(moveNode.getAttribute('id'));
        for (let i = 0; i < childrenId.length; i++) {
            let endId = childrenId[i];
            let endNode = document.getElementById(endId);
            if (document.getElementById(moveNode.getAttribute('id') + endId + 're') != undefined) {
                document.getElementById(moveNode.getAttribute('id') + endId + 're').remove();
                document.getElementById(moveNode.getAttribute('id') + endId + 'reArrow').remove();
                o.createSimMethodLink(moveNode, endNode, '-');
            }else{
                o.createSimMethodLink(moveNode, endNode, '-');
            };

        };

        childrenId = o.allNodeEnds.get(moveNode.getAttribute('id'));
        for (let i = 0; i < childrenId.length; i++) {
            let endId = childrenId[i];
            let endNode = document.getElementById(endId);
            if (document.getElementById(  endId +moveNode.getAttribute('id')+ 're') != undefined) {
                document.getElementById(endId +moveNode.getAttribute('id')+ 're').remove();
                document.getElementById(endId +moveNode.getAttribute('id')+ 'reArrow').remove();
                o.createSimMethodLink(endNode, moveNode, '-');
            }else{
                o.createSimMethodLink(endNode, moveNode, '-');
            };
        };
    };

    o.reDrawLines = function(){
        o.allNodeStarts.forEach(function(value,nodeKey){
            let pmoveNode = document.getElementById(nodeKey);
            let childrenId = o.allNodeStarts.get(nodeKey);
            for (let i = 0; i < childrenId.length; i++) {
                let endId = childrenId[i];
                let endNode = document.getElementById(endId);
                if (document.getElementById(pmoveNode.getAttribute('id') + endId + 're') != undefined) {
                    document.getElementById(pmoveNode.getAttribute('id') + endId + 're').remove();
                    document.getElementById(pmoveNode.getAttribute('id') + endId + 'reArrow').remove();
                    o.createSimMethodLink(pmoveNode, endNode, '-');
                }else{
                    o.createSimMethodLink(pmoveNode, endNode, '-');
                };

            };
        });

        o.allNodeEnds.forEach(function(value,nodeKey){
            let pmoveNode = document.getElementById(nodeKey);
            let childrenId = o.allNodeEnds.get(nodeKey);
            for (let i = 0; i < childrenId.length; i++) {
                let endId = childrenId[i];
                let endNode = document.getElementById(endId);
                if (document.getElementById(  endId +pmoveNode.getAttribute('id')+ 're') != undefined) {
                    document.getElementById(endId +pmoveNode.getAttribute('id')+ 're').remove();
                    document.getElementById(endId +pmoveNode.getAttribute('id')+ 'reArrow').remove();
                    o.createSimMethodLink(endNode, pmoveNode, '-');
                }else{
                    o.createSimMethodLink(endNode, pmoveNode, '-');
                };
            };
        });

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
           o.reDrawLines(o.moveNode);
        }else if (o.isDragBack==true) {
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
            });
            o.reDrawLines();
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