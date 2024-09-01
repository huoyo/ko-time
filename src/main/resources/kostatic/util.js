function getSize(size) {
    let sizes = [' Bytes', ' KB', ' MB', ' GB',
        ' TB', ' PB', ' EB', ' ZB', ' YB'];

    for (let i = 1; i < sizes.length; i++) {
        if (size < Math.pow(1024, i))
            return (Math.round((size / Math.pow(1024, i - 1)) * 100) / 100) + sizes[i - 1];
    }
    return size;
}

function getDom(id){
    return document.getElementById(id)
}

function post(url,data,successfun,errorFun) {
    fetch(url, {
        method: 'post',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(json => {
            successfun(json);
        }).catch(e => {
        if (errorFun) {
            errorFun(e);
        }
    });
}

function postFormData(url,data,successfun,errorFun) {
    fetch(url, {
        method: 'post',
        body: data
    }).then(response => response.json())
        .then(json => {
            successfun(json);
        }).catch(e => {
        if (errorFun) {
            errorFun(e);
        }
    });
}

function get(url,fun) {
    fetch(url).then(response => response.json())
        .then(json => {
            fun(json)
        }).catch(e => {
            console.error(e)
    })
}

function noticeSuccess(message){
    UIkit.notification.closeAll();
    UIkit.notification("<font color='green'>"+message+"</font>", {});
}

function noticeError(message){
    UIkit.notification.closeAll();
    UIkit.notification("<font color='red'>"+message+"</font>", {});
}