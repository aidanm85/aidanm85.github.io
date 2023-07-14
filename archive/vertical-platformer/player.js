class Player{
	constructor(position){
		this.position = position;
		this.velocity = {
			x: 0,
			y: 1
		};
		this.width = 100;
		this.height = 100;
	}
	draw(){
		c.fillStyle = 'red';
		c.fillRect(this.position.x, this.position.y, this.width, this.height);
	}
	update(){
		this.draw();
		this.position.y += this.velocity.y;
		if(this.position.y + this.height > canvas.height) this.velocity.y = 0;
		else this.velocity.y += gravity;
		
		this.position.x += this.velocity.x;

	}
}