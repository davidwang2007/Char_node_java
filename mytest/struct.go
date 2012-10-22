package main
import "fmt"
type Vertex struct{
	X int
	Y int
}
func main(){
	fmt.Println(Vertex{1,2})
	var _2nd Vertex = Vertex{3,4}
	fmt.Println(_2nd)
}
