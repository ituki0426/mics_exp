#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct stab {
  int val;
  char name[20];
} stab[100];
int stabuse = 0;

extern char *yytext;

#define T_STLIST    1
#define T_ASSIGN    2
#define T_READ      3
#define T_PRINT     4
#define T_ADD       5
#define T_SUB       6
#define T_NUM       7
#define T_VAR       8
#define T_LIST_VAR  9
#define T_WHILE     10
#define T_LT        11
#define T_GT        12
#define T_LE        13
#define T_GE        14
#define T_EQ        15
#define T_PP        16
#define T_CASE      17
#define T_SWITCH    18
#define T_DEFAULT   19
#define T_IF        20
#define T_IFELSE    21
#define T_STMT_PAIR 22
#define T_MUL       23
#define T_DIV       24
#define T_MOD       25

typedef struct Node {
    int node_type;
    struct Node* left;
    struct Node* right;
} Node;

/* 文字列 name[index] を生成 */
char* make_indexed_name(const char* name, int index) {
    char buf[256];
    snprintf(buf, sizeof(buf), "%s[%d]", name, index);
    return strdup(buf);
}

/* シンボルテーブル: s があれば位置を返し、なければ追加して位置を返す */
int lookup(char *s) {
  int i;
  for (i = 0; i < stabuse; ++i) {
    if (strcmp(stab[i].name, s) == 0) {
      return i;
    }
  }
  if (stabuse >= 99) {
    printf("table overflow,\n");
    exit(1);
  }
  strcpy(stab[stabuse].name, s);
  return stabuse++;
}

/* 新しいノードを作る関数 */
Node* createNode(int node_type, Node* left, Node* right) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->node_type = node_type;
    newNode->left = left;
    newNode->right = right;
    return newNode;
}


void _emit_c(Node* node) {
  if (node == NULL) {
    return;
  }
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { _emit_c(node->left); }
      _emit_c(node->right);
      break;
    /*while文の実装*/
    case T_WHILE:
        printf("\twhile (");
        _emit_c(node->left); 
        printf(") {\n");
        _emit_c(node->right); 
        printf("\t}\n");
        break;
    /*=による割り当ての実装*/
    case T_ASSIGN:
      printf("\tv%d = ", node->left);
      _emit_c(node->right);
      printf(";\n");
      break;
    /*readならscanfで文字入力*/
    case T_READ:
      printf("\tscanf(\"%%d\", &v%d);\n", node->left);
      break;
    /*printの実装*/
    case T_PRINT:
      printf("\tprintf(\"%%d\",");
      _emit_c(node->left);
      printf(");\n");
      break;
    /*加算の実装*/
    case T_ADD:
      _emit_c(node->left);
      printf(" + ");
      _emit_c(node->right);
      break;
    /*引き算の実装*/
    case T_SUB:
      _emit_c(node->left);
      printf(" - ");
      _emit_c(node->right);
      break;
    /*掛け算の実装*/
    case T_MUL:
      _emit_c(node->left);
      printf(" * ");
      _emit_c(node->right);
      break;
    /*割り算の実装*/
    case T_DIV:
      _emit_c(node->left);
      printf("/");
      _emit_c(node->right);
      break;
    /*余り演算の実装*/
    case T_MOD:
      _emit_c(node->left); 
      printf(" %% ");
      _emit_c(node->right);
      break;
    /*数値ならば値をprintするだけ*/
    case T_NUM:
      printf("%d", node->left);
      break;
    /*変数ならばv+変数番号を出力*/
    case T_VAR:
      printf("v%d", node->left);
      break;
    case T_LT:
      _emit_c(node->left);
      printf("<");
      _emit_c(node->right);
      break;
    /*大なりの実装*/
    case T_GT:
      _emit_c(node->left);
      printf(">");
      _emit_c(node->right);
      break;
    /*小なりイコールの実装*/
    case T_LE:
      _emit_c(node->left);
      printf("<=");
      _emit_c(node->right);
      break;
    /*大なりイコールの実装*/
    case T_GE:
      _emit_c(node->left);
      printf(">=");
      _emit_c(node->right);
      break;
    /*等しいの実装*/
    case T_EQ:
      _emit_c(node->left);
       printf("==");
      _emit_c(node->right);
      break;
    case T_PP:
      printf("v%d++", node->left);
      break;

    /*SWITCH文の定義*/
    case T_SWITCH: {
        printf("\tswitch (");
        _emit_c(node->left);     /* 判定式 */
        printf(") {\n");
        _emit_c(node->right);    /* case / default 連結リスト */
        printf("\t}\n");
        break;
    }
    /*各caseの出力*/
    case T_CASE:
        printf("\tcase ");
        _emit_c(node->left);           /* ラベル値 */
        printf(":\n");
        _emit_c(node->right);          /* 本体 */
        printf("\t\tbreak;\n");
        break;
    /*defaultの出力*/
    case T_DEFAULT:
        printf("\tdefault:\n");
        _emit_c(node->right);          /* 本体 */
        printf("\t\tbreak;\n");
        break;
    case T_IF:
      printf("\tif (");
      _emit_c(node->left);   // 条件式
      printf(") {\n");
      _emit_c(node->right);  // then 節
      printf("\t}\n");
      break;
    case T_IFELSE: {
      printf("\tif (");
      _emit_c(node->left);   // 条件式
      printf(") {\n");
      Node* pair = node->right;
      _emit_c(pair->left);   // then 節
      printf("\t} else {\n");
      _emit_c(pair->right);  // else 節
      printf("\t}\n");
      break;
    }
  }
}

void emit_c(Node* node) {
  printf("#include <stdio.h>\n");
  printf("int main() {\n");
  int i;
  for (i = 0; i < stabuse; i++) {
    printf("\tint v%d;\n", i);
  }
  _emit_c(node);
  printf("}\n");
}

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}

