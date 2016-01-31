#include <stdlib.h>
#include <stdio.h>
int game(); 
int dice_roll(); 
int main(){
	char name[50];
	char play[5];
	int i;
	printf("Welcome to the DeFranco Casino\n");
	printf("Please enter your name: ");
	scanf("%s",name);
	printf("%s, would you like to Play or Quit? ",name);
	scanf("%s",play);
	for(i = 0; play[i]; i++){
		tolower(play[i]);
	}
	if(strcmp(play,"play") == 0){
		FILE *f;
		f = fopen("/dev/dice","rb");
		game(f);
		fclose(f);
	}
	printf("\nGoodbye, %s\n",name);	
	
	return 0;
}

game(FILE *fp){
	char yes[5];
	int i;
	int dice1 = dice_roll(fp);
	int dice2 = dice_roll(fp);
	int point = dice1 + dice2;
	int cont = 1;

	printf("\nYou have rolled %d + %d = %d\n",dice1,dice2,point);
	if(point == 7 || point == 11){
		printf("You Win!\n");
	}
	else if(point == 2 || point == 3 || point == 12){
		printf("You Lose!\n");
	}
	else{
		printf("The point is %d\n",point);
		while(cont == 1){
			dice1 = dice_roll(fp);
			dice2 = dice_roll(fp);
			printf("\nYou have rolled %d + %d = %d\n",dice1,dice2,(dice1 + dice2));
			if((dice1+dice2) == 7){
				printf("You Lose!\n");
				cont = 0;
			}
			else if((dice1+dice2) == point){
				printf("You Win!\n");
				cont = 0;
			}
			else{
				printf("Still in the game\n");
			}

		}
	}
	printf("\nWould you like to play again?\n");
	scanf("%s",yes);
	for(i = 0; yes[i]; i++){
		tolower(yes[i]);
	}
	if(strcmp(yes,"yes") == 0){
		game(fp);
	} 
}

dice_roll(FILE *fp){
	char number = 0;
	fread(&number,sizeof(number),1,fp);
	return (number + 1);
}

