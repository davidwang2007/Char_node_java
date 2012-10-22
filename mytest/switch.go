/*************************************************************************
	> File Name: switch.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 07:12:29 PM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){
	i := 1
	switch i {
		case 1:
			fmt.Println("case 1")
			fallthrough
		case 2:
			fmt.Println("case 2")
			fallthrough
		default:
			fmt.Println("default");
	}
}
