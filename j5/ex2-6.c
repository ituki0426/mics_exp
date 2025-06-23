#include <stdio.h>
struct stab {
  int val;
  char name[20];
} stab[100];
int stabuse = 0;

extern char *yytext;

#define T_STLIST 1
#define T_ASSIGN 2
#define T_READ   3
#define T_PRINT  4
#define T_ADD    5
#define T_SUB    6
#define T_NUM    7
#define T_VAR    8

typedef struct Node {
    int node_type;
    struct Node* left;
    struct Node* right;
} Node;


// 引数にとる文字列が表にあれば見つかった位置を返して，なければ追加する関数
int lookup(char *s) {
  int i;
  // 表の使っているところを順に見ていって
  for (i = 0; i < stabuse; ++i) {
    if (strcmp(stab[i].name, s) == 0) {
      // 見つかった場合はその位置を返す
      return i;
    }
  }
  if (stabuse >= 99) {
    // 変数の上限の個数を超えてしまった
    printf("table overflow,\n");
    exit(1);
  }

  // 表になかったので追加する
  strcpy(stab[stabuse].name, s);
  return stabuse++;  // 追加した位置を返す
}

// 新しいノードを作る関数
Node* createNode(int node_type, Node* left, Node* right) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->node_type = node_type;
    newNode->left = left;
    newNode->right = right;
    return newNode;
}

// ノードをたどる関数
void traverse_tree(Node* node) {
  if (node == NULL) {
    return;
  }
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { traverse_tree(node->left); printf("; "); }
      traverse_tree(node->right);
      break;
    case T_ASSIGN:
      printf("v%d = ", node->left);
      traverse_tree(node->right);
      printf("\n");
      break;
    case T_READ:
      printf("read v%d\n", node->left);
      break;
    case T_PRINT:
      printf("print ");
      traverse_tree(node->left);
      printf("\n");
      break;
    case T_ADD:
      printf("(");
      traverse_tree(node->left);
      printf(" + ");
      traverse_tree(node->right);
      printf(")");
      break;
    case T_SUB:
      printf("(");
      traverse_tree(node->left);
      printf(" - ");
      traverse_tree(node->right);
      printf(")");
      break;
    case T_NUM:
      printf("%d", node->left);
      break;
    case T_VAR:
      printf("v%d", node->left);
      break;
  }
}

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}
