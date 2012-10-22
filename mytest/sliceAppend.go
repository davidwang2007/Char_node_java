/*************************************************************************
	> File Name: sliceAppend.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Sun 21 Oct 2012 11:51:27 PM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){
	s1 := []int{0,1,2}
	s2 := append(s1,3)
	s3 := append(s1,s2...)
	fmt.Println(s1,"<<>>",s2,"<<>>",s3)
}
