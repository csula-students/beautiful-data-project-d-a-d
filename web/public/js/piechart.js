
function createPie(){

  console.log('baking the pie');

  var pieElement = document.getElementById('pieChart');


 var width = $( window ).width()  * 0.60,
      height = $( window ).height()  * 0.60,
      radius = Math.min(width, height) / 2;


  // var color = d3.scale.ordinal()
  //     .range(["orange", "steelblue", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);


      var color = d3.scale.ordinal()
      //.range(["#3366cc", "#f44336", "#ff9900", "#4caf50", "#0099c6", "#dd4477", "#66aa00", "#b82e2e", "#316395", "#994499", "#22aa99", "#aaaa11", "#6633cc", "#e67300", "#8b0707", "#651067", "#329262", "#5574a6", "#3b3eac"]);
      .range(['#66C2A4', '#78C679', '#8C95C6', '#7BCCC4', '#FC8D59']);
  var arc = d3.svg.arc()
      .outerRadius(radius - 10)
      .innerRadius(0);

  var labelArc = d3.svg.arc()
      .outerRadius(radius - 40)
      .innerRadius(radius - 40);

  var pie = d3.layout.pie()
      .sort(null)
      .value(function(d) { return d.population; });




  var svg = d3.select(pieElement).append("svg")
      .attr("width", width)
      .attr("height", height)
    .append("g")
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");



  d3.csv("/data/pie.csv", function(error, data) {
    if (error) throw error;

    console.log(data);

    var g = svg.selectAll(".arc")
        .data(pie(data))
      .enter().append("g")
        .attr("class", "arc");

    g.append("path")
        .attr("d", arc)
        .style("fill", function(d) { return color(d.data.type); });

    g.append("text")
        .attr("transform", function(d) { return "translate(" + labelArc.centroid(d) + ")"; })
        .attr("dy", ".35em")
        .style("fill", "white")
        .style("font-size","15px")
        .text(function(d) { return d.data.type; });
  });



}


function type(d) {
  d.population = +d.population;
  return d;
}
