loadList();
//create buttons and functions for when they're clicked
var addButton = document.getElementById("add-button");
addButton.addEventListener("click", addToDoItem);

function addToDoItem() {
  var itemText = toDoEntryBox.value;
  newToDoItem(itemText,  false);
}

var clearButton = document.getElementById("clear-completed-button");
clearButton.addEventListener("click", clearCompletedToDoItems);

function  clearCompletedToDoItems(){
  var completedItems = toDoList.getElementsByClassName("completed");
  
  while (completedItems.length > 0){
    completedItems.item(0).remove();
  }
}



var saveButton = document.getElementById("save-button");
saveButton.addEventListener("click", saveList);

function saveList() {
  var toDos = [];
  for(var i = 0; i < toDoList.children.length; i++){
    var toDo = toDoList.children.item(i);
    
    var toDoInfo = {
      "task" : toDo.innerText,
      "completed" : toDo.classList.contains("completed")
    };
    toDos.push(toDoInfo);
  }
  localStorage.setItem("toDos",  JSON.stringify(toDos));
}

//create vars for the text box and html list
var toDoEntryBox = document.getElementById("todo-entry-box");
var toDoList = document.getElementById("todo-list");

function newToDoItem(itemText, completed){
  var toDoItem = document.createElement("li");
  var toDoText = document.createTextNode(itemText);
  toDoItem.appendChild(toDoText);
  
  if(completed){
    toDoItem.classList.add("completed");
  }
  
  toDoList.appendChild(toDoItem);
  toDoItem.addEventListener("dblclick",  toggleToDoItemState);
  
}

function toggleToDoItemState(){
  if(this.classList.contains("completed")) this.classList.remove("completed");
  else this.classList.add("completed");
}

var emptyListButton = document.getElementById("empty-button");
emptyListButton.addEventListener("click", emptyList);

function emptyList(){
  var toDoItems = toDoList.children;
  while(toDoItems.length > 0){
    toDoItems.item(0).remove();
  }
}

function loadList(){
  if(localStorage.getItem("toDos")  != null){
    var toDos = JSON.parse(localStorage.getItem("toDos"));
    
    for(var i = 0; i < toDos.length; i++){
      var toDo = toDos[i];
      newToDoItem(toDo.task, toDo.completed);
    }
  }
}

// load current chart package
google.charts.load("current", {
  packages: ["corechart", "line"]
});
// set callback function when api loaded
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
  // create data object with default value
  let data = google.visualization.arrayToDataTable([
    ['Time', 'CPU Usage', 'RAM'],
    [0, 0, 0],
  ]);
  // create options object with titles, colors, etc.
  let options = {
    title: "CPU Usage",
    hAxis: {
      textPosition: 'none',
    },
    vAxis: {
      title: "Usage"
    }
  };
  // draw chart on load
  let chart = new google.visualization.LineChart(
    document.getElementById("chart_div")
  );
  chart.draw(data, options);
}
