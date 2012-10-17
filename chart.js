var stdin = process.stdin;
var net = require('net');
var server = net.createServer(function(socket){
	console.log('接收到来自'+socket.remoteAddress+':'+socket.remotePort+' 客户端的连接');
	socket.on('data',function(chunk){
		console.log('>>' + chunk.toString().replace(/(\r|\n)/g,''));
	});
	socket.on('end',function(){
		console.log('关闭输出流');
	});
	
	socket.on('error',function(error){
		console.log('发生错误' + error);
		socket.end();
		socket.destroy();
	});
	socket.on('close',function(){
		console.log('连接关闭');
	});
	//
	stdin.resume();
	stdin.setEncoding('utf8');
	stdin.on('data',function(chunk){
		console.log('calling socket write ' + chunk);
		socket.write(chunk);
	});
	stdin.on('end',function(){
		socket.destroy();
	})
});
server.listen(9009);
console.log('服务器启动，监听端口9009');
