package main
import "fmt"
func main(){
	p := []int{2,3,5,7,11,13}
	fmt.Println("p = ",p)
	fs := "p[%d] = %d\n"
	for i:= 0; i < len(p); i++{
		fmt.Printf(fs,i,p[i])
	}
}
