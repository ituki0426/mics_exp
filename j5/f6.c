#include <stdio.h>
struct stab {
  int val;
  char name[20];
} stab[100];
int stabuse = 0;

extern char *yytext;
#include <stdint.h> 
#define INT_PTR(i) ((Node*)(intptr_t)(i))
#define PTR_INT(p) ((int)(intptr_t)(p))

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

// ASTに変更を加える関数 (例: T_ADDノードの左右の子を交換する)
Node* opt(Node* node,Node* parent, char lr) {
  if (node == NULL) {
    printf("node is NULL");
    return NULL;
  }
  // 現在のノードに対する処理
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { opt(node->left,node,"l");}
      opt(node->right,node,'r');
      break;
    case T_ASSIGN:
      opt(node->right,node,'r');
      break;
    case T_PRINT:
      opt(node->left,node,'l');
      break;
    case T_ADD:
      if (node->left->node_type == T_NUM && node->right->node_type == T_NUM) {
         Node* newNode = createNode(T_NUM, INT_PTR(PTR_INT(node->left->left) + PTR_INT(node->right->left)), NULL);
         if (lr == 'r') parent->right = newNode;
         else parent->left  = newNode;
      }else if(node->left->node_type == T_NUM && node->right->node_type == T_VAR && PTR_INT(node->left->left) == 0){
        if(lr == 'r') parent->right = node->right;
        else parent->left  = node->right;
      }else if(node->left->node_type == T_VAR && node->right->node_type == T_NUM && PTR_INT(node->right->left) == 0){
        if(lr == 'r') parent->right = node->left;
        else parent->left  = node->left;
      }else{
        opt(node->left,node,'l');
        opt(node->right,node,'r');
      }
    break;

      break;
  }
  return node; // 変更後の(あるいは変更しない)ノードを返す
}

// ノードをたどる関数
void traverse_tree(Node* node) {
  if (node == NULL) {
    printf("node is NULL\n");
    return;
  }
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { traverse_tree(node->left); printf("; "); }
      traverse_tree(node->right);
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
      printf("%d", PTR_INT(node->left));
      break;
    case T_VAR:
      printf("v%d", PTR_INT(node->left));
      break;
    case T_ASSIGN:
       /* the left child is a VAR node – print it properly */
      traverse_tree(node->left);
      printf(" = ");
      traverse_tree(node->right);
      printf("\n");
      break;

  }
}

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}
