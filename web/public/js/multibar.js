
var barElement = document.getElementById('barChart');


// var margin = {top: 80, right: 80, bottom: 80, left: 80},
//     width = 600 - margin.left - margin.right,
//     height = 400 - margin.top - margin.bottom;

var w =  $( window ).width();
var h =   $( window ).height();
var m =  {top: 80, right: 50, bottom: 80, left: 80};

if ( w < 800)
{

  w =   $( window ).width() * 0.80;
  h =   $( window ).height() * 0.50;
  m =  {top: 80, right: 10, bottom: 80, left: 50};
}
else
{
  w = w * 0.60 - m.left - m.right;
  h = h * 0.60 - m.top - m.bottom;
}

var margin = m,
    width = w,
    height = h;


var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);
var y0 = d3.scale.linear().domain([300, 1100]).range([height, 0]),
    y1 = d3.scale.linear().domain([300, 1100]).range([height, 0]);
var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");
// create left yAxis
var yAxisLeft = d3.svg.axis().scale(y0).ticks(4).orient("left");
// create right yAxis
var yAxisRight = d3.svg.axis().scale(y1).ticks(6).orient("right");




var svg = d3.select(barElement).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("class", "graph")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
d3.csv("/data/bars.csv", function(error, data) {
  console.log(data);
  x.domain(data.map(function(d) { return d.field; }));
  y0.domain([0, d3.max(data, function(d) { return d.first; })]);
  
  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);
  svg.append("g")
	  .attr("class", "y axis axisLeft")
	  .attr("transform", "translate(0,0)")
	  .call(yAxisLeft)
	.append("text")
	  .attr("y", 6)
	  .attr("dy", "-2em")
	  .style("text-anchor", "end")
	  .style("text-anchor", "end")
	  .text("Count");
	

  bars = svg.selectAll(".bar").data(data).enter();
  bars.append("rect")
      .attr("class", "bar1")
      .style("fill", 'steelblue')
      .attr("x", function(d) { return x(d.field); })
      .attr("width", x.rangeBand()/2)
      .attr("y", function(d) { return y0(d.first); })
	  .attr("height", function(d,i,j) { return height - y0(d.first); }); 
  bars.append("rect")
      .attr("class", "bar2")
      .style("fill", 'orange')
      // .style("fill", '#29b6f6')
      .attr("x", function(d) { return x(d.field) + x.rangeBand()/2; })
      .attr("width", x.rangeBand() / 2)
      .attr("y", function(d) { return y0(d.second); })
	  .attr("height", function(d,i,j) { return height - y0(d.second); }); 
});

function type(d) {
  d.money = +d.money;
  return d;
}
