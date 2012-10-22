/*************************************************************************
	> File Name: fizzbuzz.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 07:25:33 PM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){

	for i:=0; i < 100; i++{
		if i % 15 == 0 {
			fmt.Printf("%d -> %s\t",i,"FuzzBuzz")
		}else if i % 3 == 0 {
			fmt.Printf("%d -> %s\t",i,"Fuzz")
		}else if i % 5 == 0 {
			fmt.Printf("%d -> %s\t",i,"Buzz")
		}
	}
}
