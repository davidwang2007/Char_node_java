package main
import "fmt"
type Vertex struct{
	Lat, Long float64
}
var m map[string]Vertex
func main(){
	m = make(map[string]Vertex)
	m["1"] = Vertex{40.6843,74.39967}
	fmt.Println(m["1"]);
}
