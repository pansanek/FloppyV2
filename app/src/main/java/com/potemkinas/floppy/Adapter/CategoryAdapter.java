package com.potemkinas.floppy.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potemkinas.floppy.CategoryPage;
import com.potemkinas.floppy.R;
import com.potemkinas.floppy.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    Context context;
    List<Category> categories;

    //конструктор
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //класс в котором указан дизайн элемента из category_item.xml
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new CategoryAdapter.CategoryViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) { //класс в котором указано, что нужно подставить в дизайн
        holder.categoryBackground.setBackgroundColor(Color.parseColor(categories.get(position).getColor()));

        int imageID=context.getResources().getIdentifier("ic_"+categories.get(position).getImg(),"drawable",context.getPackageName());
        holder.categoryImage.setImageResource(imageID);

        holder.categoryTitle.setText(categories.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //функция, которая при нажатии на элемент переходит на страницу категории, которой передает свои свойства
                Intent intent = new Intent(context, CategoryPage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context, new Pair<>(holder.categoryImage,"categoryImage"));

                intent.putExtra("categoryBg",Color.parseColor(categories.get(position).getColor()));
                intent.putExtra("categoryImage",imageID);
                intent.putExtra("categoryTitle",categories.get(position).getTitle());


                context.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{
        LinearLayout categoryBackground;
        ImageView categoryImage;
        TextView categoryTitle;
        public CategoryViewHolder(@NonNull View itemView) { //функция нахождения элементов дизайна
            super(itemView);

            categoryBackground=itemView.findViewById(R.id.categoryBackground);
            categoryImage=itemView.findViewById(R.id.categoryImage);
            categoryTitle=itemView.findViewById(R.id.categoryTitle);


        }
    }
}
