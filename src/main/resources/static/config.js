function getOption(data,threshold){
    return  {
        tooltip: {
            trigger: 'item',
            triggerOn: 'mousemove'
        },
        series: [
            {
                type: 'tree',
                // initialTreeDepth: 3,
                data: [data],
                top: '1%',
                left: '15%',
                bottom: '1%',
                right: '10%',
                roam: true,
                symbolSize: 20,
                itemStyle: {
                    borderColor: 'green'
                },

                label: {
                    position: 'right',
                    formatter: function(params){
                        var bg = "titleBgGreen"
                        if (params.value>threshold) {
                            bg = "titleBgRed"
                        }
                        showList =  [
                            '{'+bg+'|  指标}',
                            '  {aa|}方法：'+params.name+" ",
                            '{hr|}',
                            '  {aa|}类型： '+params.data.methodType+" ",
                            '{hr|}',
                            '  {aa|}平均耗时： '+params.data.avgRunTime+" ms ",
                            '{hr|}',
                            '  {aa|}最大耗时： '+params.data.maxRunTime+" ms ",
                            '{hr|}',
                            '  {aa|}最小耗时： '+params.data.minRunTime+" ms ",
                            '{hr|}',
                            '  {aa|}异常数目： '+params.data.exceptionNum+" 个 ",
                        ]
                        if (params.data.exceptionNum>0) {
                            for (let i = 0; i < params.data.exceptions.length; i++) {
                                var exception = params.data.exceptions[i]
                                showList.push('{hr|}')
                                showList.push('  {warn|}异常详情：'+exception.name+' ('+exception.location+'行)'+' '+exception.message+' ')
                            }
                        }

                        return showList.join('\n');
                    },
                    backgroundColor: '#ddd',
                    borderColor: '#88e781',
                    borderWidth: 1,
                    borderRadius: 5,
                    color: '#000',
                    fontSize: 12,
                    rich: {
                        titleBgGreen: {
                            align: 'left',
                            backgroundColor: '#59977e',
                            height: 20,
                            borderRadius: [5, 5, 0, 0],
                            padding: [0, 0, 0, 0],
                            width: '100%',
                            color: '#eee'
                        },
                        titleBgRed: {
                            align: 'left',
                            backgroundColor: '#dc1d16',
                            height: 20,
                            borderRadius: [5, 5, 0, 0],
                            padding: [0, 0, 0, 0],
                            width: '100%',
                            color: '#eee'
                        },
                        hr: {
                            borderColor: '#777',
                            width: '100%',
                            borderWidth: 0.5,
                            height: 0
                        },
                        aa: {
                            lineHeight: 20,
                            borderColor: '#111111',
                            height: 20,
                            borderRadius: [5, 5, 0, 0],
                            padding: [0, 0, 0, 0],
                            width: '0%'

                        },
                        warn: {
                            lineHeight: 20,
                            borderColor: '#111111',
                            backgroundColor: '#7e8d5b',
                            height: 20,
                            borderRadius: [5, 5, 0, 0],
                            padding: [0, 0, 0, 0],
                            width: '0%'

                        },
                        t: {
                            align: 'center'
                        }
                    }
                },

                leaves: {
                    label: {
                        position: 'right',
                        verticalAlign: 'middle',
                        align: 'left'
                    }
                },
                expandAndCollapse: true,
                animationDuration: 550,
                animationDurationUpdate: 750
            }
        ]
    }
}

