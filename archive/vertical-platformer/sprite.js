class Sprite{
	constructor({position, imgSrc}){
		this.position = position;
		this.image = new Image();
		this.image.src = imgSrc;
	}
	draw(){
		if(!this.image) return
		c.drawImage(this.image, this.position.x, this.position.y)
	}
	update(){
		this.draw();
	}
}