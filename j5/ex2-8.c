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
#define T_WHILE  9
#define T_LT     10
#define T_GT     11
#define T_PP     12

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
int dotree(Node* node) {
  if (node == NULL) {
    return 0;
  }
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { dotree(node->left); }
      dotree(node->right);
      break;
    case T_ASSIGN:
      stab[(int)(node->left)].val = dotree(node->right);
      break;
    case T_READ:
      scanf("%d", &stab[(int)(node->left)].val);
      break;
    case T_PRINT:
      printf("%d\n", dotree(node->left));
      break;
    case T_ADD:
      return dotree(node->left) + dotree(node->right);
    case T_SUB:
      return dotree(node->left) - dotree(node->right);
    case T_NUM:
      return node->left;
    case T_VAR:
      return stab[(int)(node->left)].val;
    case T_WHILE:
      while(dotree(node->left)) {
        dotree(node->right);
      }
      break;
    case T_LT:
      return dotree(node->left) < dotree(node->right);
    case T_GT:
      return dotree(node->left) > dotree(node->right);
    case T_PP:
      int v = stab[(int)(node->left)].val;
      stab[(int)(node->left)].val = v + 1;
      return v;
  }
}

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}
