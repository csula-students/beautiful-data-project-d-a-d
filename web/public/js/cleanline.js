

registerKeyboardHandler = function(callback) {
  var callback = callback;
  d3.select(window).on("keydown", callback);
};


MultiGraph = function(elemid, title, xlabel, ylabel) {
  var self = this;

  d3.csv("/data/clean.csv", function(error, data) {

    //console.log(data);

    var options = {
      'title':  title,
      'xlabel': xlabel,
      'ylabel': ylabel,
    };

    var fields = [];
    var names = [];
    var iso = 0;

    date = new Date();
    var xmin = date;
    var xmax = 0;
    var ymin = 100000000;
    var ymax = 0;

  
    data.forEach(function(d) {
      var field = d.field.trim();
      var value = parseInt(d.value);
      var date = new Date(d.date);
      iso = +date;

      var nameIndex = names.indexOf(field);

      if( nameIndex < 0){
        var obj = {'type':field, 'data':[]};
        fields.push(obj);
        names.push(field);
      }
      else{
      }
      
      if(nameIndex > 0){
        fields[nameIndex]['data'].push({'date':iso, 'value':value});
      }
      else{
        fields[fields.length -1]['data'].push({'date':iso, 'value':value});
      }
      

      if(xmin > iso){
        xmin = iso;
      }
      else{
        if(xmax < iso)
          xmax = iso;
      }


      if(ymin > value){
        ymin = value;
      }
      else {
        if(ymax < value)
          ymax = value;
      }

    });
      
        
    
    options['dataPoints'] = fields;
    options['xmax'] =  new Date(xmax);
    options['xmin'] =  new Date(xmin);
    options['ymax'] = ymax;
    options['ymin'] = ymin;


    console.log(options);

    if(options.dataPoints.length > 0)
    {
      self.generate(options, elemid);

    }
  });



};


function myIndex(myList, type) {
  if(myList.length > 0)
  {
        myList.forEach(function(i, element){
          if(element.type == type)
          {
            return i;
          }
        });
  }
  else
  {
    return -1;
  }

}


  MultiGraph.prototype.generate = function(options, elemid) {
      
    //captures the reference to this (the DOM) because it can change depending on
    //the scope
    var self = this;
    this.chart = document.getElementById(elemid);
    //generates the size of the graph based on the window size
    this.cx=$(window).width()-320;
    this.cy= this.cx / 1.5;
    this.legendwidth = 170;
    //captures some of the options, or sets them to defaults if they don't exist
    //x is the date range that will be draw (x-axis)
    //y is the range of y-axis that will be drawn
    this.options = options || {};
    this.options.xmax = options.xmax || 30;
    this.options.xmin = options.xmin || 0;
    this.options.ymax = options.ymax || 20;
    this.options.ymin = options.ymin || -2;

    //adds padding around the graph for styling purposes
    this.padding = {
       "top":    this.options.title  ? 40 : 20,
       "right":                 30,
       "bottom": this.options.xlabel ? 60 : 10,
       "left":   this.options.ylabel ? 70 : 45
    };

    //calculate the width and height, after allocation from window size and
    //space reserved for padding, title and axis labels
    this.size = {
      "width":  this.cx - this.padding.left - this.padding.right - this.legendwidth,
      "height": this.cy - this.padding.top  - this.padding.bottom
    };

    //the d3 transformation for x-axis (time)
    //converts time/date passed from option into pixel value
    this.x = d3.time.scale()
      .domain([this.options.xmin, this.options.xmax])
      .range([0, this.size.width]);


    // drag x-axis logic
    this.downx = Math.NaN;

    //the d3 transformation for the y-axis (noise value)
    //converts the dB value into pixel value
    this.y = d3.scale.linear()
        .domain([this.options.ymax, this.options.ymin])
        .nice()
        .range([0, this.size.height])
        .nice();

    // drag y-axis logic
    this.downy = Math.NaN;

    this.dragged = this.selected = null;

    //contains the information for drawing a noise line
    this.line = d3.svg.line()
        .x(function(d, i) { return this.x(d.date); })
        .y(function(d, i) { return this.y(d.value); })
        .interpolate("linear");

        //contains the information for drawin threshold line
      

    var xrange =  (this.options.xmax - this.options.xmin),
        yrange2 = (this.options.ymax - this.options.ymin) / 2,
        yrange4 = yrange2 / 2,
        datacount = this.size.width/30;

     

      //the main chart element
      //everything else adds onto this element
    this.vis = d3.select(this.chart).append("svg")
        .attr('class','theChart')
        .attr("width",  this.cx)
        .attr("height", this.cy)
        .append("g")
          .attr("transform", "translate(" + this.padding.left + "," + this.padding.top + ")");

    //the gray graph area
    this.plot = this.vis.append("rect")
        .attr("width", this.size.width)
        .attr("height", this.size.height)
        .style("fill", "#f5f5f5")
        .attr("pointer-events", "all")
        .on("mousedown.drag", self.plot_drag())
        .on("touchstart.drag", self.plot_drag())
        this.plot.call(d3.behavior.zoom().x(this.x).y(this.y).on("zoom", this.redraw()));
        self = this;

     //create empty text for min/max information
     this.vis.append("text")
       .attr("x", 0)
       .attr("y", 0)
       .attr("id", "info_name")
       .text("");
     this.vis.append("text")
       .attr("x", 0)
       .attr("y", 14)
       .attr("id", "info_min")
       .text("");
     this.vis.append("text")
       .attr("x", 0)
       .attr("y", 28)
       .attr("id", "info_max")
       .text("");
     this.vis.append("text")
       .attr("x", 0)
       .attr("y", 42)
       .attr("id", "info_avg")
       .text("");

       //ordinal scale
        //generates a color for a given key
        //used to give each line a different color
     this.colorScale = d3.scale.category10();


 //copies datapoints from options into another variable
      this.fields = options.dataPoints;
      console.log(this.fields);

  

      //the gridlines on the graph
      this.fields.forEach(function(line){
           self.vis.append("svg")
            .attr("top", 0)
            .attr("left", 0)
            .attr("width", self.size.width)
            .attr("height", self.size.height)
            .attr("viewBox", "0 0 "+self.size.width+" "+self.size.height)
            .attr("class", "line")
            .append("path")
                .attr("class", "line")
                .attr("d", self.line(line.data));   
      });
    

    // add Chart Title
    if (this.options.title) {
      this.vis.append("text")
          .attr("class", "axis")
          .text(this.options.title)
          .attr("x", this.size.width/2)
          .attr("dy","-0.8em")
          .style("text-anchor","middle");
    }

    // Add the x-axis label
    if (this.options.xlabel) {
      this.vis.append("text")
          .attr("class", "axis")
          .text(this.options.xlabel)
          .attr("x", this.size.width/2)
          .attr("y", this.size.height)
          .attr("dy","2.4em")
          .style("text-anchor","middle");

    }

    // add y-axis label
    if (this.options.ylabel) {
      this.vis.append("g").append("text")
          .attr("class", "axis")
          .text(this.options.ylabel)
          .style("text-anchor","middle")
          .attr("transform","translate(" + -40 + " " + this.size.height/2+") rotate(-90)");
    }

    //connects the mouse events to the relevant functions
    d3.select(this.chart)
        .on("mousemove.drag", self.mousemove())
        .on("touchmove.drag", self.mousemove())
        .on("mouseup.drag",   self.mouseup())
        .on("touchend.drag",  self.mouseup());

    this.redraw()();
  };




//Implements the functionality for panning
MultiGraph.prototype.plot_drag = function() {
  var self = this;
  return function() {
    registerKeyboardHandler(self.keydown());
    d3.select('body').style("cursor", "move");
    if (d3.event.altKey) {
      var p = d3.svg.mouse(self.vis.node());
      var newpoint = {};
      newpoint.x = self.x.invert(Math.max(0, Math.min(self.size.width,  p[0])));
      newpoint.y = self.y.invert(Math.max(0, Math.min(self.size.height, p[1])));
      self.points.push(newpoint);
      self.points.sort(function(a, b) {
        if (a.x < b.x) { return -1 };
        if (a.x > b.x) { return  1 };
        return 0
      });
      self.selected = newpoint;
      self.update();
      d3.event.preventDefault();
      d3.event.stopPropagation();
    }
  }
};

//Updates the graph in the event of any changes
MultiGraph.prototype.update = function() {

  var self = this;
  //First it removes the old trendlines
  this.vis.selectAll("path").remove();

  //-------------------------------------------------------------------------------------
  this.colorScale = d3.scale.category10();
  self = this;//capture the value held by this so it doesn't change
  //draw all trending lines

var p = {'x':0, 'y':0};

 $(document).mousemove(function(event) {
        p.x = event.pageX;
        p.y = event.pageY;
    });

    //Handles the tooltip- the pop when you mouseover the lines
    var tip = d3.tip()
      .attr('class', 'd3-tip')
      .attr('id', 'removable')
      .html(function(d) {
        console.log(d);
        return "<strong>Industry Field:</strong> <span style='color:red'>" + d.type + "</span>"
        + '<br /><strong>Min: </strong>' + d3.min(d.data.filter(function(d) {
            return self.x(d.date) > 0 && self.x(d.date) < self.cx;
        }), function(d){ return +d.value; })
        + '<br /><strong>Max: </strong>' + d3.max(d.data.filter(function(d) {
            return self.x(d.date) > 0 && self.x(d.date) < self.cx;
        }), function(d){ return +d.value; })
        + '<br /><strong>Avg: </strong>' + d3.mean(d.data.filter(function(d) {
            return self.x(d.date) > 0 && self.x(d.date) < self.cx;
        }), function(d){ return +d.value; });
      });

  var div = d3.select("body").append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

this.vis.call(tip);

//redraw the lines
this.fields.forEach(function(line){
    self.vis.append("svg")
        .attr("top", 0)
        .attr("left", 0)
        .attr("width", self.size.width)
        .attr("height", self.size.height)
        .attr("viewBox", "0 0 "+self.size.width+" "+self.size.height)
        .attr("class", "line")
        .append("path")
          .attr("id", 'hello')
          .attr("class", "line")
          .style("stroke", self.colorScale(line.type))
          .on("mouseover", function(d) {//display tooltip on mouseover
            tip.show(line);
            d3.select("#removable")
              .style("top", d3.event.pageY + "px")
              .style("left", d3.event.pageX + "px");
            d3.select(d3.event.target)
              .style("stroke-width", 3);
            })
          .on("mouseout", function(d) {//remove tooltip on mouseout
            tip.hide(line);
            d3.select(d3.event.target)
              .style("stroke-width", 1);
          })
           
          .attr("d", self.line(line.data));
});

//draw legend
var offset = 1;//the number of legends drawn, each one moves slightly more
var distance = 14;//number of pixels between each legends
self = this;
this.fields.forEach(function(line){
    self.vis.append("text")
      .attr("x", self.size.width + 12)
      .attr("y", offset * distance)
      .text(line.type)
      .style("fill", self.colorScale(line.type));

    self.vis.append("svg")
      .append("rect")
      .attr("x", self.size.width)
      .attr("y", offset * distance - 10)
      .attr("width", 10)
      .attr("height", 10)
      .on("mouseover", function() {
        tip.show(line);
        d3.select("#removable")
          .style("top", d3.event.pageY  +"px")
          .style("left", d3.event.pageX + "px");
        d3.select("#" + line.type)
          .style("stroke-width", 3);
          console.log("mouseover");
      })
      .on("mouseout", function() {
        tip.hide(line);
        d3.select("#" + line.type)
          .style("stroke-width", 1);
      })
      .style("Fill", self.colorScale(line.type));
    offset = offset + 1;
});

//Following code is need to differentiate click and drag events
  //see http://stackoverflow.com/questions/19075381/d3-mouse-events-click-dragend
  //for more information
  if (d3.event && d3.event.keyCode) {
    d3.event.preventDefault();
    d3.event.stopPropagation();
  }
}

MultiGraph.prototype.datapoint_drag = function() {
  var self = this;
  return function(d) {
    registerKeyboardHandler(self.keydown());

    self.update();

  }
};

//Handles mousemove event
MultiGraph.prototype.mousemove = function() {
  var self = this;
  return function() {
    var p = d3.mouse(self.vis[0][0]),
        t = d3.event.changedTouches;

    if (self.dragged) {
      self.dragged.y = self.y.invert(Math.max(0, Math.min(self.size.height, p[1])));
      self.update();
    };
    if (!isNaN(self.downx)) {
      d3.select('body').style("cursor", "ew-resize");
      var rupx = self.x.invert(p[0]),
          xaxis1 = self.x.domain()[0],
          xaxis2 = self.x.domain()[1],
          xextent = xaxis2 - xaxis1;
      if (rupx != 0) {
        var changex, new_domain;
        changex = self.downx / rupx;
        new_domain = [xaxis1, xaxis1 + (xextent * changex)];
        self.x.domain(new_domain);
        self.redraw()();
      }
      d3.event.preventDefault();
      d3.event.stopPropagation();
    };
    if (!isNaN(self.downy)) {
      d3.select('body').style("cursor", "ns-resize");
      var rupy = self.y.invert(p[1]),
          yaxis1 = self.y.domain()[1],
          yaxis2 = self.y.domain()[0],
          yextent = yaxis2 - yaxis1;
      if (rupy != 0) {
        var changey, new_domain;
        changey = self.downy / rupy;
        new_domain = [yaxis1 + (yextent * changey), yaxis1];
        self.y.domain(new_domain);
        self.redraw()();
      }
      d3.event.preventDefault();
      d3.event.stopPropagation();
    }
  }
};

MultiGraph.prototype.mouseup = function() {
  var self = this;
  return function() {
    document.onselectstart = function() { return true; };
    d3.select('body').style("cursor", "auto");
    d3.select('body').style("cursor", "auto");
    if (!isNaN(self.downx)) {
      self.redraw()();
      self.downx = Math.NaN;
      d3.event.preventDefault();
      d3.event.stopPropagation();
    };
    if (!isNaN(self.downy)) {
      self.redraw()();
      self.downy = Math.NaN;
      d3.event.preventDefault();
      d3.event.stopPropagation();
    }
    if (self.dragged) {
      self.dragged = null
    }
  }
}

//Handles key events
//Currently only handles backspace and delete
MultiGraph.prototype.keydown = function() {
  var self = this;
  return function() {
    if (!self.selected) return;
    switch (d3.event.keyCode) {
      case 8: // backspace
      case 46: { // delete
        var i = self.points.indexOf(self.selected);
        self.points.splice(i, 1);
        self.selected = self.points.length ? self.points[i > 0 ? i - 1 : 0] : null;
        self.update();
        break;
      }
    }
  }
};

//Handles redraw after visual elements change
MultiGraph.prototype.redraw = function() {
  var self = this;
  return function() {
    var tx = function(d) {
      return "translate(" + self.x(d) + ",0)";
    },
    ty = function(d) {
      return "translate(0," + self.y(d) + ")";
    },
    stroke = function(d) {
      return d ? "#ccc" : "#666";
    },
    fx = self.x.tickFormat(10),
    fy = self.y.tickFormat(10);

    //remove all lingering tooltips so that they don't linger
    $('#removable').remove();

    // Regenerate x-ticks…
    var gx = self.vis.selectAll("g.x")
        .data(self.x.ticks(7), String)
        .attr("transform", tx);

    gx.select("text")
        .text(fx);

    var gxe = gx.enter().insert("g", "a")
        .attr("class", "x")
        .attr("transform", tx);

    gxe.append("line")
        .attr("stroke", stroke)
        .attr("y1", 0)
        .attr("y2", self.size.height);

    gxe.append("text")
        .attr("class", "axis")
        .attr("y", self.size.height)
        .attr("dy", "1em")
        .attr("text-anchor", "middle")
        .text(fx)
        .on("mouseover", function(d) { d3.select(this).style("font-weight", "bold");})
        .on("mouseout",  function(d) { d3.select(this).style("font-weight", "normal");});

    gx.exit().remove();

    // Regenerate y-ticks…
    var gy = self.vis.selectAll("g.y")
        .data(self.y.ticks(7), String)
        .attr("transform", ty);

    gy.select("text")
        .text(fy);

    var gye = gy.enter().insert("g", "a")
        .attr("class", "y")
        .attr("transform", ty)
        .attr("background-fill", "#FFEEB6");

    gye.append("line")
        .attr("stroke", stroke)
        .attr("x1", 0)
        .attr("x2", self.size.width);

    gye.append("text")
        .attr("class", "axis")
        .attr("x", -3)
        .attr("dy", ".35em")
        .attr("text-anchor", "end")
        .text(fy)
        .style("cursor", "ns-resize")
        .on("mouseover", function(d) { d3.select(this).style("font-weight", "bold");})
        .on("mouseout",  function(d) { d3.select(this).style("font-weight", "normal");})
        .on("mousedown.drag",  self.yaxis_drag())
        .on("touchstart.drag", self.yaxis_drag());

    gy.exit().remove();
    self.plot.call(d3.behavior.zoom().x(self.x).y(self.y).on("zoom", self.redraw()));
    self.update();
  }
}

//Stores updated x-direction info in self.downx
MultiGraph.prototype.xaxis_drag = function() {
  var self = this;
  return function(d) {
    document.onselectstart = function() { return false; };
    var p = d3.mouse(self.vis[0][0]);
    self.downx = self.x.invert(p[0]);
  }
};

//Stores updated y-direction info in self.downy

MultiGraph.prototype.yaxis_drag = function(d) {
  var self = this;
  return function(d) {
    document.onselectstart = function() { return false; };
    var p = d3.mouse(self.vis[0][0]);
    self.downy = self.y.invert(p[1]);
  }
};



var graph = new MultiGraph('lineGraph', 'Popularity Over Time', 'Days', 'Count');


