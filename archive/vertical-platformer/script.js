const canvas = document.querySelector('canvas');
const c = canvas.getContext('2d');

canvas.width = 1024;
canvas.height = 576;
c.fillStyle = 'white';


const floorCollisions2D = [];
for(let i = 0; i < floorCollisions.length; i+= 36;){
	floorCollisions2D.push(floorCollisions.slice(i, i + 36);
}

floorCollisions2D.forEach((row) => {
	row.forEach((symbol) => {
		if(symbol === 202){
			console.log('draw a block here!');
		}
	})
})
const gravity = 0.5;

c.fillRect(0, 0, canvas.width, canvas.height);



const p1 = new Player({
	x: 300, 
	y: 100
});
	
const p2 = new Player({
	x:0,
	y:0
});

const keys ={
	d:{
		pressed: false
	},
	a:{
		pressed: false
	}
};

const background = new Sprite({
	position:{
		x:0,
		y:0
	},
	imgSrc: './img/background.png',
});

function animate(){
	window.requestAnimationFrame(animate);
	console.log('animating');
	//clear canvas
	c.fillStyle = 'white';
	c.fillRect(0, 0, canvas.width, canvas.height);
	
	c.save();
	c.scale(4,4);
	c.translate(0, -background.image.height + canvas.height/4);
	background.update();
	c.restore();

	p1.update();
	p2.update();
	
	p1.velocity.x = 0;
	p2. velocity.x = 0;
	if(keys.d.pressed) p1.velocity.x = 5;
	if(keys.a.pressed) p1.velocity.x = -5;
	
}

animate();
window.addEventListener('keydown', (event) => {
	switch(event.key){
		case 'd': 
			keys.d.pressed = true;
		break;
		case 'a': 
			keys.a.pressed = true;
		break;
		case 'w': 
			p1.velocity.y = -15;
		break;
	}
})

window.addEventListener('keyup', (event) => {
	switch(event.key){
		case 'd': 
			keys.d.pressed = false;
		break;
		case 'a': 
			keys.a.pressed = false;
		break;
	}
})