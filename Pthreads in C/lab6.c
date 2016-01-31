#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void *do_stuff(void *p){
	while(1){
		printf("Hello from thread %d - A\n", *(int *)p);
		pthread_yield();
		printf("Hello from thread %d - B\n", *(int *)p);	 
	}
}

int main(){
	pthread_t thread;
	int id,arg1,arg2;
	
	arg1 = 2;
	id = pthread_create(&thread,NULL,do_stuff,(void *)&arg1);
	arg2 = 1;
	do_stuff((void *)&arg2);
	pthread_join(thread,NULL);
	return 0;
}
	

	
