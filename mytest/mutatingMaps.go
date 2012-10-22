package main
import "fmt"
func main(){
	m := make(map[string]int)
	m["answer"] = 42
	fmt.Println(m["answer"])
	delete(m,"answer")
	fmt.Println(m["answer"])
	v,ok := m["answer"]
	fmt.Println("The value:",v,"Present?",ok)
}
