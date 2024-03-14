package com.chimallidigital.solucionint.ui.selector.dialogue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles

class DialogueArticlesSelectorAdapter(
    private var articlesList: List<CollectionArticles> = emptyList(),
    private var itemOnSelected: (CollectionArticles) -> Unit,
    private var newLambda:() -> Unit
) :
    RecyclerView.Adapter<DialogueArticlesSelectorViewHolder>() {
        fun initLambda(lambda:()->Unit){
            newLambda= lambda
        }
        fun updateSelectorList(lista: List<CollectionArticles>){
            articlesList= lista
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DialogueArticlesSelectorViewHolder {
        return DialogueArticlesSelectorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialogue_articles_selector, parent, false)
        )
    }

    override fun getItemCount() = articlesList.size

    override fun onBindViewHolder(holder: DialogueArticlesSelectorViewHolder, position: Int) {
        holder.render(articlesList[position], itemOnSelected, newLambda)
    }
}