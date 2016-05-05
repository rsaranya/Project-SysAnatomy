﻿
function displayHeatMap()
{
    var sourceData = [];
    var firstDate = new Date();
    firstDate.setDate(firstDate.getDate() - 28);
    firstDate.setHours(0, 0, 0, 0);

    for (var i = 0; i < 28; i++) {
        var newDate = new Date(firstDate);
        newDate.setDate(newDate.getDate() + i);
        var dataPoint = {
            date: newDate
        }

        // generate value for each hour
        for (var h = 0; h <= 23; h++) {
            dataPoint['value' + h] = Math.round(Math.random() * 4);
        }

        sourceData.push(dataPoint);
    }

    // now let's populate the source data with the colors based on the value
    // as well as replace the original value with 1
    var colors = ['#FF0000', '#ff6666', '#FCD1D1', '#800000', '#A52A2A' ];
    for (i in sourceData) {
        for (var h = 0; h <= 23; h++) {
            sourceData[i]['color' + h] = colors[sourceData[i]['value' + h]];
            sourceData[i]['hour' + h] = 1;
        }
    }

    // define graph objects for each hour
    var graphs = [];
    for (var h = 0; h <= 23; h++) {
        graphs.push({
            "balloonText": "Original value: [[value" + h + "]]",
            "fillAlphas": 1,
            "lineAlpha": 0,
            "type": "column",
            "colorField": "color" + h,
            "valueField": "hour" + h
        });
    }

    var chart = AmCharts.makeChart("chartdiv", {
        "type": "serial",
        "dataProvider": sourceData,
        "valueAxes": [{
            "stackType": "regular",
            "axisAlpha": 0.3,
            "gridAlpha": 0,
            "maximum": 24,
            "duration": "mm",
            "unit": ":00"
        }],
        "graphs": graphs,
        "columnWidth": 1,
        "categoryField": "date",
        "categoryAxis": {
            "parseDates": true,
            "gridPosition": "start",
            "axisAlpha": 0,
            "gridAlpha": 0,
            "position": "left"
        }
    });



}


