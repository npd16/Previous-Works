#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

int shell_cd(char **args);
int shell_exit(char **args);

char *builtin_str[] = { /*Names of Built in Commands*/
	"cd",
	"exit"
};

int (*builtin_func[]) (char **) = { /*Corresponding Functions*/
	&shell_cd,
	&shell_exit
};

int shell_num_builtins() {
	return sizeof(builtin_str) / sizeof(char *);
}

int shell_cd(char **args){
	if (args[1] == NULL){
		fprintf(stderr, "expected argument to \"cd\"\n");
	}
	else {
		if(chdir(args[1]) != 0){
			perror("NO SUCH DIRECTORY");
		}
	}
	return 1;
}

int shell_exit(char **args){
	return 0;
}

int shell_launch(char **args,char *file, int or, int oa, int ir){
	pid_t pid, wpid;
	int status;

	pid = fork();
	if (pid == 0){

		if(or){
			freopen(file,"w+",stdout);
		}
		else if(oa){
			freopen(file,"a+",stdout);
		}		
		else if(ir){
			freopen(file,"r",stdin);
		}

		if(execvp(args[0],args) == -1){
			perror("ERROR WHILE EXECUTING");
		}
		exit(EXIT_FAILURE);
	}
	else if (pid < 0){
		perror("ERROR WHILE FORKING");
	}
	else {
		do{
			wpid = waitpid(pid,&status,WUNTRACED);
		}
		while(!WIFEXITED(status) && !WIFSIGNALED(status));
	}
	
	return 1;
}

char** shell_parse(char *line){
	char **argu = malloc(128 * sizeof(char*));
	char d[12] = "\r \n\t()|&;";
	char *tok;
	int i = 0;
	tok  = strtok(line,d);
	while(tok != NULL){
		argu[i] = tok;
		tok = strtok(NULL,d);
		i++;
	}
	argu[i] = NULL;
	return argu; 
}

int shell_execute(char **args){
	int i,status,or,oa,ir;
	int num = shell_num_builtins();
	char *line;

	if(args[0] == NULL){
		return 1;
	}
	
	for(i = 0; i < num; i++){
		if(strcmp(args[0],builtin_str[i]) == 0){
			return (*builtin_func[i])(args);
		}
	}
		
	i = 1;
	or = 0;
	oa = 0;
	ir = 0;
	do{
		line = args[i];
		if(strcmp(line,">") == 0){
			or = 1;
			line = args[i+1];
			while(args[i-1] != NULL){
				args[i] = args[i+2];
				i++;
			}
		}
		else if(strcmp(line,">>") == 0){
			oa = 1;
			line = args[i+1];
			while(args[i-1] != NULL){
				args[i] = args[i+2];
				i++;
			}
		}
		else if(strcmp(line,"<") == 0){
			ir = 1;
			line = args[i+1];
			while(args[i-1] != NULL){
				args[i] = args[i+2];
				i++;
			}
		}
		i++;
	}
	while(args[i] != NULL);
	
	return shell_launch(args,line,or,oa,ir);
}

void shell_loop(void){
	char *buffer[512];
	char **arguments;
	int status;

	do{
		printf("> ");
		fgets(buffer,512,stdin);
		arguments = shell_parse(buffer);
		status = shell_execute(arguments);

		free(arguments);
	}
	while(status);
}



int main (){

	shell_loop();

	return 0;
}	
