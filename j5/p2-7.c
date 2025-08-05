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

/* switch-case文用: T_STLIST を再帰的に探索し、与えられた値 val に一致する CASE を実行。
   default があれば default_case に保持 */
int trav_cases(Node* parent, int val, Node **default_case){
    if (parent == NULL) return 0;
    if (parent->node_type == T_STLIST) {
        if (trav_cases(parent->left, val, default_case)) return 1;
        if (trav_cases(parent->right, val, default_case)) return 1;
    }
    if (parent->node_type == T_CASE) {
        if (dotree(parent->left) == val) {
            dotree(parent->right);
            return 1;
        }
    } else if (parent->node_type == T_DEFAULT) {
        *default_case = parent;
    }
    return 0;
}

/* AST を評価・実行する関数 */
int dotree(Node* node) {
  if (node == NULL) {
    return 0;
  }
  switch (node->node_type) {
    case T_STLIST:
      if (node->left != NULL) { dotree(node->left); }
      dotree(node->right);
      break;

    case T_SWITCH: {
      Node* default_case = NULL;
      if (trav_cases(node->right, dotree(node->left), &default_case) == 0 && default_case != NULL) {
        dotree(default_case->right);
      }
      break;
    }

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
      return (int)(node->left);   /* left に値を詰めている設計 */

    case T_VAR:
      return stab[(int)(node->left)].val;

    case T_WHILE:
      while (dotree(node->left)) {
        dotree(node->right);
      }
      break;

    case T_LT:
      return dotree(node->left) < dotree(node->right);

    case T_GT:
      return dotree(node->left) > dotree(node->right);

    case T_LE:
      return dotree(node->left) <= dotree(node->right);

    case T_GE:
      return dotree(node->left) >= dotree(node->right);

    case T_EQ:
      return dotree(node->left) == dotree(node->right);

    case T_PP: {
      int v = stab[(int)(node->left)].val;
      stab[(int)(node->left)].val = v + 1;
      return v;
    }

    case T_IF:
      if (dotree(node->left)) {  /* 条件が真なら右(then節)を実行 */
        dotree(node->right);
      }
      break;

    case T_IFELSE: {
      int cond = dotree(node->left);
      Node* pair = node->right; /* pair->left = then節, pair->right = else節 */
      if (cond) {
        dotree(pair->left);
      } else {
        dotree(pair->right);
      }
      break;
    }
  }
  return 0;
}

#include "y.tab.c"
#include "lex.yy.c"

int main() {
  yyparse();
  return 0;
}
