document.addEventListener("DOMContentLoaded", function() {
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var chartData = window.chartData || '[]';
        console.log("ChartData en la plantilla desde js: ", chartData); 
        var data = google.visualization.arrayToDataTable(JSON.parse(chartData));

        var options = {
            title: 'Movies by Rating',
            is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
        chart.draw(data, options);
    }
});
