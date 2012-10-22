/*************************************************************************
	> File Name: sliceCopy.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 12:02:19 AM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){
	s1 := [...]int{0,1,2,3,4,5}
	s2 := make([]int,9)
	len1 := copy(s2,s1[0:])
	fmt.Println(s2,"<<>>",s1,len1)
	len2 := copy(s2,s1[3:])
	fmt.Println(s2,"<<>>",s1,len2)
}
